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
## API 개발 고급

### @PostConstruct

```java
package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
조회용 샘플 데이터 입력
- userA
    - JPA1 Book
    - JPA2 Book
- userB
    - SPRING1 BOOK
    - SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    @PostConstruct
    public void init() {

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit1(){ //DB에 샘플데이터 저장하려는 매서드
            Member member = new Member();
            member.setName("UserA");
            member.setAddress(new Address("부산", "해운대", "23-1"));
            em.persist(member);
        }
    }

}

```

1. **스프링 컨테이너가 InitDb를 빈으로 등록한다**
    
    → @Component 덕분에 InitDb가 스프링 Bean으로 등록됨
    
2. **InitDb의 생성자가 호출되면서 InitService가 주입된다**
    
    → @RequiredArgsConstructor 덕분에 final InitService initService 생성자 주입
    
    → 이 시점에 InitService 의존성 주입이 완료됨
    
3. **@PostConstruct 메서드(init)가 실행된다**
    
    → 의존성 주입이 끝난 뒤, 스프링이 자동으로 init() 메서드 호출
    
    → initService.dbInit1() 메서드를 호출함
    
4. **initService.dbInit1() 메서드가 실행되는데, 이때 트랜잭션이 적용된다**
    
    → InitService 클래스에 @Transactional이 붙어 있어서 프록시(Proxy)가 트랜잭션을 관리
    
    → 메서드 실행 전에 **트랜잭션 시작 → 메서드 끝나면 commit/rollback 처리**
    
5. **em.persist()가 트랜잭션 안에서 실행된다 (영속성 컨텍스트에 Member 등록)**
    
    → 트랜잭션 커밋 시점에 JPA가 flush → DB에 insert 쿼리 실행
### 엔티티 설계(*ToOne 성능 최적화)

> *ToOne 관계는 연관 엔티티 조회시, N+1 문제나 불필요한 쿼리 증가 가능성이 있어, 성능최적화가 필요함.
> 

성능 문제가 생기는 이유

- ManyToOne, OneToOne 관계는 기본적으로 **즉시 로딩(Eager Loading)** 으로 설정됨
- 즉, **부모 엔티티를 조회할 때 연관된 엔티티까지 한꺼번에 SELECT** 해 오려고 함
- 이때 연관된 엔티티를 **Join** 해서 가져오거나, 별도의 **추가 SELECT** 로 가져옴
- → **불필요한 쿼리 실행** 발생 → 성능 저하 가능성

```java
List<Order> orders = orderRepository.findAll(); //주문 100개를 조회한다고 할때
for (Order order : orders) {
		System.out.println(order.getMember().getName());
} // 각 Order마다 연관된 Member를 가져옴(추가쿼리) -> N+1 문제 발생
 
```

- 필요하지 않는 정보까지 불러오게 되면서 API 스펙에 맞지않을 가능성도 있음.

Fetch Join를 사용해서 조회를 하면, JPA는 SQL Join을 그대로 사용함.

```java
//JPA 
return em.createQuery(
    "SELECT o FROM Order o JOIN o.orderItems oi", Order.class
).getResultList();

//SQL
SELECT o.*
FROM orders o
JOIN order_item oi ON o.id = oi.order_id
```

문제1)  Order (1) ↔ OrderItem (N) 관계일 때:

- 하나의 Order가 여러 개의 OrderItem을 갖고 있다면,
- JPQL JOIN FETCH를 사용하면 **SQL 조인**이 발생하고,
- **SQL 결과는 조인된 수만큼 Order 정보가 중복**되어 내려옴.
- Order ID = 1 이고, OrderItem이 3개라면, Order ID = 1이 3번 반복됨.
- **동일한 Order ID라도** 조인된 row가 다르면 **다른 객체로 인식할 수 있음** → 중복된 Order 객체가 생성됨

문제1 해결)

1. SQL 레벨:
    - SQL 쿼리에 DISTINCT가 붙어 중복 row 제거 (정확히는 SELECT DISTINCT ... 형태로 실행됨)
2. JPA 레벨 (중요):
    - JPA는 **엔티티의 식별자(PK)** 기준으로 **중복된 엔티티 인스턴스를 메모리에서 자동 제거**
    - 즉, 같은 Order ID를 가진 객체는 **한 번만 조회되어 컬렉션에 담김**

```java
//JPA 
return em.createQuery(
    "SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems", Order.class
).getResultList();

//SQL
SELECT DISTINCT o.*
FROM orders o
JOIN order_items oi ON o.id = oi.order_id
```

문제2) 컬렉션 페치 조인을 사용하면, 페이징이 불가능하다. 

- Order 1개에 OrderItem이 3개 있다고 하면, JPA가 Order와 OrderItem을 **Fetch Join**하면, **조인된 row가 3개**가 됨 (Order가 3번 반복됨).
- 이런 중복된 row 때문에 **페이징의 기준이 깨져버림**.
- Hibernate, 쿼리에는 LIMIT , OFFSET을 적용하지않음. 대신 경고 로그를 출력함.
    
    `firstResult/maxResults specified with collection fetch; applying in memory!`
    
- 즉, DB에서 전체 데이터를 다 읽고, 메모리상에서 페이징 처리. ⇒ 비효율적, 데이터가 많으면 OutOfMemory 위험도 있음.

> 페이징 API
JPA는 페이징 처리시, `setFirstResult` , `setMaxResults` 매서드로 추상화 하고. 
내부적으로 동작되는 쿼리는 JPA에 설정한 Database 방언에 맞게 실행됨. 
⇒ 정리하자면 페이징 API는 JPA가 조회 쿼리의 결과를 원하는 범위로 제한하도록 지원하는 기능이며, 내부적으로 DB에 맞는 방식으로 LIMIT/OFFSET 등을 적용하는것.
> 
> 
> ```java
> setFirstResult(int startPosition) // startPosition : 조회할 시작 위치
> setMaxResults(int maxResult) // maxResult : 조회할 데이터 수
> ```
> 

문제해결2) 

- 컬렉션은 Fetch Join을 하지않고, LAZY 로딩 + Batch Size를 사용함.
- 또는 DTO에서 직접 조회해서 쿼리를 튜닝하는 방법.

[참고] 

- 모든 객체는 서로 참조를 통해, 마치 그래프처럼 연결되있다. 자바에서 .(점)을 찍어서 연관된 객체로 이동할 수 있다.
- JPQL에서도 경로 표현식을 통해 객체 그래프를 탐색할 수 있다.
1. 상태 필드
    - JPQL
    `select m.username from Member m`
        - Member 객체의 username 필드의 접근하기 위한. (Member 객체의 연관객체인 username 이동할수있다는것)
    - SQL
    `SELECT m.username FROM member m`

→ 문자열이나 숫자처럼 단순한 값을 저장하는 필드. 더이상 탐색이 불가능. 

1. 단일값 연관 필드
- `@OneToOne`  , `@ManyToOne` 연관관계를 맺은 필드. 엔티티이기때문에,추가 탐색이 가능함. 묵시적 내부 조인이 발생한다.
1. `@OneToMany,` `@ManyToMany` 을 통해 연관관계를 맺은 필드. 탐색 결과는 컬렉션이다. 추가 탐색이 불가능하다. 묵시적 내부조인이 발생한다. 

- 묵시적 내부조인 : JPQL에는 조인이 명시되어있지 않지만, SQL에서는 조인이 생기는것. 연관된 엔티티는 다른 테이블에 저장되어 있기때문에, 조인을 통해 가지고와야한다.
- 묵시적 조인은, 내부조인이 되는 한계가 있다.
- JPQL은 변환되는 SQL과 최대한 모영을 비슷하게 맞춰주는것이 유지보수하기에도 좋다.
- 그래서 묵시적 조인 대신 명시적 조인을 사용한다.

### 관심사 분리

**화면에 보여지는 API(Presentation Layer API)**

- 사용자 인터페이스(UI) 와 직접 연결되는 API
- 클라이언트에서 데이터를 보여주기 위해 호출.
- 조회용(Read) 많음.
- 성능이나 응답속도가 중요함.
- REST API & GraphQL 형태로 많이 구성됨.

**중요 서비스 로직 (Core Business Logic API)**

- 비즈니스의 핵심을 처리함.
- 데이터 생성, 수정, 삭제 및 중요한 비즈니스 규칙.
- 도메인 중심 설계

## OSIV와 성능 최적화

- Open EntityManager In View : JPA 에서 OSIV를 부르는 용어 (관례상 OSIV라 함)
- OSIV(Open Session In View) : 영속성 컨텍스트를 뷰 렌더링이 끝날 때까지 열어두는것(트랜잭션이 끝나고나서도)
- JPA의 영속성 컨텍스트가 DB 커넥션을 얻는 시점 ⇒ DB 트랜잭션이 시작될때 (@Transactional) 어노테이션이 붙은 매서드가 실행이 될 때.
- `spring.jpa.open-in-view: true` 일때
    - EntityManager (영속성 컨텍스트) 가 Controller 또는 View(Rendering) 까지 살아있음.
    - 서비스 매서드 (@Transactional) 이 끝나도 DB 커넥션 반납하지 않고, View에서 Lazy 로딩 가능하게 함.
    
    ```java
    @GetMapping("/orders")
    public List<Order> orders() {
        List<Order> orders = orderService.findAll(); // 이때는 member 안 불러옴
        return orders; // 여기서 member.getName() 같은 접근이 일어나면 Lazy 로딩 발생 => Member 조회
    }
    ```
    
- `spring.jpa.open-in-view: false`  일때
    - 서비스 계층(@Transactional) 이 끝나는 순간, EntityManager 닫히고 DB 커넥션도 반환
    - 그 이후에 Lazy 로딩을 시도하면 예외(LazyInitializationException) 발생
    - 트랜잭션이 끝나기전에 강제로 지연로딩해야함
    
    ```java
    @Transactional
    public List<OrderDto> findAllOrders() {
        List<Order> orders = orderRepository.findAllWithMemberAndItems(); // fetch join 등으로 다 미리 로딩
        return orders.stream().map(...).collect(toList()); //서비스에 필요한 모든 데이터를 가지고와야함.
    } //@Transaction이 끝남.
    ```
    
- Command 와 Query 분리
    - 실무에서 OSIV를 끈 상태로 복잡성을 관리하기 좋은 방법
    - 보통 비즈니스 로직은 특정 엔티티 몇개를 등록하거나 수정하는 것이므로, 성능이 크게 문제가 되진않지만, 복잡한 화면을 출력하기 위한 쿼리는 화면에 맞추어 성능을 최적화하는것이 중요
    - 관심사 분리.
    - 고객 서비스의 실시간 API는 OSIV를 끄고, ADMIN 처럼 커넥션을 많이 사용하지 않는 곳에서는 OSIV를 켠다.