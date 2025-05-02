package jpabook.jpashop.Member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class MemberSignUpTestRequest {
    private String name;

    public MemberSignUpTestRequest(String name) {
        this.name = name;
    }
}
