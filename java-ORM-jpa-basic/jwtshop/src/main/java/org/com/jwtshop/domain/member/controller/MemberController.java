package org.com.jwtshop.domain.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.com.jwtshop.domain.member.dto.MemberSignUpRequest;
import org.com.jwtshop.domain.member.dto.MemberUpdateRequest;
import org.com.jwtshop.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.com.jwtshop.global.response.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor //생성자 주입 - 의존성 주입
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "사용자의 정보를 전달받아 회원 가입 진행")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createMember(@Validated @RequestBody MemberSignUpRequest memberSignUpRequest) { //@Validated : 서로 다른 여러 필드를 선택적으로 검증하기 위해
        return ResponseEntity.ok(ApiResponse.of(memberService.saveMember(memberSignUpRequest)));
    }

    @Operation(summary = "회원 정보 수정", description = "사용자의 ID 값을 전달받아, 정보를 수정할 회원을 찾고 회원정보수정을 진행")
    @PatchMapping("/{id}") //일부를 수정할때는 PatchMapping
    public ResponseEntity<ApiResponse<Long>> updateMember(@PathVariable Long id,  @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return ResponseEntity.ok(ApiResponse.of(memberService.updateMember(id, memberUpdateRequest)));

    }




}
