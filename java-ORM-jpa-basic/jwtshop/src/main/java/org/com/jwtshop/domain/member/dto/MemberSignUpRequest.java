package org.com.jwtshop.domain.member.dto;

import lombok.Getter;
import org.com.jwtshop.domain.member.domain.Address;
import org.com.jwtshop.domain.member.domain.Member;

@Getter

public class MemberSignUpRequest {
    private String name;
    private String city;
    private String street;
    private String zipcode;
    private String phone;

    public Member toEntity() {
        return Member
                .builder()
                .name(name)
                .address(new Address(city,street,zipcode))
                .phone(phone)
                .build();
    }
}
