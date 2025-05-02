package org.com.jwtshop.domain.member.dto;

import lombok.Getter;
import org.com.jwtshop.domain.member.domain.Address;
import org.com.jwtshop.domain.member.domain.Member;

@Getter
public class MemberUpdateRequest {
    //회원 수정 : 이름 수정 - 전화번호 수정 - 주소 수정
    private String option;
    private String value;

}
