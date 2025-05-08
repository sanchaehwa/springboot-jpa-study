package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor

public class MemberApiController {
    private final MemberService memberService;

    //회원 등록 API
    @PostMapping("/api/v1/members") // V1: 엔티티를 그대로 요청 본문에 사용 (지양)
    public CreateMemberResponse saveMemberv1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberv2(@RequestBody @Valid  CreateMemberRequest request){ // V2: 요청에 DTO 사용 → 엔티티 노출 X (권장)
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    //회원 수정 API´
    @PutMapping("/api/v2/members/{id}") //수정할 데이터의 ID를 URL에서 가져옴
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
            //변경감지 (JPA에서 엔티티 값이 변경되었음을 감지해서,Transaction이 끝날 때, 자동으로 SQL에 Update 쿼리를 날려주는)
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
    //회원 조회 API(전쳬)
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }
    //회원 조회 API - DTO
    @GetMapping("/api/v2/members")
    public Result memberV2() {
          //List 형태로 값을 가져오는것은 같음
        List<Member> findMembers = memberService.findMembers(); //Member 엔티티 조회
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName())) //엔티티 객체() -> 원하는 필드(name)만 담은 DTO로 변환 ->list
                .collect(Collectors.toList());

        return new Result(collect); //DTO -> Reault 객체로 랩핑


    }


    //회원 등록 DTO
    //요청 DTO
    @Data
    static class CreateMemberRequest {
        private String name;
    }
    //응답 DTO
    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
    //회원 수정 DTO
    //요청 DTO
    @Data
    @AllArgsConstructor
    static class UpdateMemberRequest {
        @NotEmpty //검증과정은 DTO에 넣는게 좋음 -> 응답, 요청 검증 조건이 다를수도 있고, 엔티티는 비즈니스 로직 중심
        private String name;

    }
    //응답 DTO
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;

    }
    //회원조회 DTO
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDTO {
         private String name;
    }



}
