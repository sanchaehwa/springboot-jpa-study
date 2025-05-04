package jpabook.jpashop.Member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.Member.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//폼 바인딩 객체 - 기본 생성자가 필요함
@NoArgsConstructor
@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    private String city;
    private String street;
    private String zipcode;

}
