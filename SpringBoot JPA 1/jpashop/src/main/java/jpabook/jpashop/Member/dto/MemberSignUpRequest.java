package jpabook.jpashop.Member.dto;

import jpabook.jpashop.Member.domain.Member;
import lombok.Getter;

@Getter
public class MemberSignUpRequest {
    private String username;

    public Member toEntity() {
        return Member
                .builder()
                .username(username)
                .build();
    }
}
