## API 개발 기본

### DTO

> DTO: 프로세스 간에 데이터를 전달하는 객체를 의미함. 비즈니스 로직과 같은 복잡한 코드는 없고 순수하게 전달하고 싶은 데이터만 담겨있음.
ex) 요청 및 응답 DTO

DTO는 요청 파라미터
: 클라이언트가 API 요청을 보낼때 같이 보내는 데이터 
`http://localhost:8080/api/v2/members` 요청을 보낼때, 데이터를 JSON 또는 쿼리 스트링으로 
전달
{
  “name” : “sam”
}
→ 이 데이터를 자바객체로 변환해줌 (DTO로 매핑)

DTO로 매핑을 한다는것은, 
JSON (요청 파라미터) → DTO 변환(매핑) → DTO로 Member Entity를 생성하는것.
> 
> 
> ```java
> @PostMapping("/api/v2/members")
> //Entity가 아닌 CreateMemberRequest(DTO)를 사용해 요청 데이터 매핑받고(DTO 로 요청 데이터를 받는것 
> // 그 값을 바탕으로 Member Entity 생성
> public CreateMemberResponse saveMember(@RequestBody CreateMemberRequest request)
> 
>    //요청 DTO
>     @Data
>     static class CreateMemberRequest {
>         private String name;
>     }
>     //응답 DTO
>     @Data
>     static class CreateMemberResponse {
>         private Long id; //요청 응답에 필요한 id 값만 전달(DTO 장점: 필요한 데이터만 선택적 골라서 전달)
>         public CreateMemberResponse(Long id){
>             this.id = id;
>         }
>     }
> ```
> 

### DTO 의 필요성

1. Entity는 내부 시스템 전용 모델
    - DB 구조나 비즈니스 로직에 맞게 설계됨.
    - 외부(클라이언트)는 알 필요 없는 정보가 많음.
    - 그대로 노출하면 보안 문제, 불필요한 정보 유출, 변경에 약한 구조가 됨.
2. Entity가 바뀌면 API도 덩달아 바뀜
    - 예: Member 엔티티에 phoneNumber 필드를 추가하면, 그걸 사용하는 API 응답(JSON)도 바뀜.
    - 클라이언트 입장에선 갑자기 응답 포맷이 바뀌면 에러 발생 가능.

⇒ DTO를 사용함으로써,

- 필요한 정보만 추려서 전달 가능. (예: id, name만 응답)
- Entity는 내부에서 바뀌더라도 **DTO는 그대로 유지 가능** → API 응답은 안정적.
- 나중에 리팩터링하기도 쉽고, 보안상 더 안전함.

### Command - Query

CORS(Command Query Responsibility Segregation) 

- Command(명령) : 데이터를 변경하는 작업 →`INSERT`, `UPDATE`, `DELETE`
- Query(조회) : 데이터를 조회하는 작업 → `SELECT`
- Command - Query 하나의 매서드 안에 같이있는 형태

```java
@Transactional
public void update(Long id, String name) {
    Member member = memberRepository.findOne(id); // 수정할 회원 찾음 → Query
    member.setName(name); // 이름 변경 (객체 상태 변경) → Command
}
// 트랜잭션 커밋 시점에 JPA가 변경 감지하여 flush() 호출 → DB에 UPDATE 쿼리 실행
```

- Command - Query 분리 ⇒ 책임 분리 (Command -Query Responsibility Segregation)

```java
    @PutMapping("/api/v2/members/{id}") //수정할 데이터의 ID를 URL에서 가져옴
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
            //Command -> 데이터를 변경하는 작업
        memberService.update(id, request.getName()); 
        //Query -> 데이터 조회
        Member findMember = memberService.findOne(id); 
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
    
    /*
	    (위에 코드)
	    Member member = memberRepository.findOne(id) ; -> 수정할 회원을 찾고(Query) member 객체에 수정 회원을 저장
	    member.setName(name); -> (Query 작업의 결과) member에 바로 Command 수정작업을 한거 -> Query, Command가 분리가 되지않음
	    
	    (Command -Query 분리코드)
	    memberService.update(id,request.getName()); -> 데이터 변경 Command
	    Member findMember = memberService.findOne(id); -> **다시 DB에서 조회**
    */

```

### 회원 정보 조회

```java
    @GetMapping("/api/v1/members")
    //List로 조회, 배열을 사용한 응답(JSON 스펙 고정화 문제발생)
    public List<Member> membersV1(){ //List 형태로 Member를 조회했기때문에 JSON 배열형태로 값이 들어왔음을 알수 있음.
        return memberService.findMembers(); //엔티티에서 Member 조회
    }
    
    //엔티티에서 Member 조회하면 생기는 문제
    /* 1. Orders의 정보를 보고싶지않다 -> JsonIgnore 에노테이션을 엔티티에 붙여주면 됨
		   1에 대한 문제점 => 다른 조회 API에서는 Orders 정보를 보고싶다 하면 JsonIgnore을 붙이면 안됨
		   (이유) JsonIgnore은 모든 API 응답에대한 무시. 그래서 컨트롤이 불가능
		 */
		 //(해결방법) DTO에서 원하는 정보만 설정해서 Member를 조회해야함 (Member 조회API 응답 전용 설계를 한다는것!)
		
		@GetMapping("/api/v2/members")
    public Result memberV2() {
          //List 형태로 값을 가져오는것은 같음
        List<Member> findMembers = memberService.findMembers(); //Member 엔티티 조회
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName())) //엔티티 객체() -> 원하는 필드(name)만 담은 DTO로 변환 ->list
                .collect(Collectors.toList());

        return new Result(collect); //DTO -> Reault 객체로 랩핑

    }
    //JSON 최상위가 되게 - 배열로 고정되어 있는 형태의 문제점 해결을 위한 랩핑
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data; //T로 설정하는것은  data 필드의 타입을 어떤걸로도 받을 수 있다는것 * 응답의 데이터 타입을 유연하게 바꿀수 있음. 
    }
    
```

(조회 결과)

```json
[ // 배열형태로 값이 들어옴
    {
        "id": 1,
        "name": "화영",
        "address": {
            "city": "부산",
            "street": "해운대",
            "zipcode": "23-1"
        },
        "orders": []
    }
]
//DTO로 변한한 List만 바로 반환하면(Result 객체로 랩핑안하고)
[
  { "name": "화영" },
  { "name": "수진" },
  { "name": "수현" }
]
//문제점 발생 -> JSON이 [] 배열 루트로 시작하면, API 스펙확장이 어렵고
//새로운 필드 추가 불가 구조가 배열로 고정되어 있다는것은 구조 변경도 어렵다는것 ->리펙토링에서 문제생김
//문제점 해결 -> Result 객체로 감싸면 JSON이 최상위 유연한 확장이 가능함
{
	  "data": [
    { "name": "화영" },
    { "name": "수현" }
  ]

}
//API 추가정보 를 넣을수있음
현
  "data": [
    { "name": "화영" },
    { "name": "수현" }
  ],
  "count": 2,
  "message": "회원 리스트 조회 성공"
}
	
```