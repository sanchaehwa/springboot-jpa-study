package org.com.jwtshop.domain.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.com.jwtshop.domain.member.dto.MemberSignUpRequest;
import org.com.jwtshop.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.com.jwtshop.global.response.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor //생성자 주입 - 의존성 주입
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "사용자의 정보를 전달받아 회원 가입 진행")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createMember(@Validated @RequestBody MemberSignUpRequest memberSignUpRequest) {
        return ResponseEntity.ok(ApiResponse.of(memberService.saveMember(memberSignUpRequest)));
    }

}
