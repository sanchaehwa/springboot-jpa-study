package org.com.jwtshop.domain.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.com.jwtshop.domain.member.dto.MemberUpdateRequest;
import org.com.jwtshop.domain.member.exception.InvaildArgumentException;
import org.com.jwtshop.domain.order.domain.Order;
import org.com.jwtshop.global.error.ErrorCode;

import java.util.*;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class Member  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //member id 자동 증가
    private Long id;

    @NotBlank(message = "이름을 입력해주세요")
    @Column(nullable=false, unique=true, length=45)
    private String name;

    @NotBlank(message = "주소를 입력해주세요")
    @Embedded
    private Address address;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Column(nullable=false, unique=true, length=45 )
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "전화번호를 올바르게 작성해주세요") //정규식 검사
    private String phone;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    //삭제 여부를 알려주는
    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted;

    public void addOrders(Order orders){
        this.orders.add(orders);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    @Builder
    private Member(Long id, String name, Address address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;

    }

    //회원 수정
    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        String value = memberUpdateRequest.getValue();
        switch (UpdateInfo.getUpdateOption(memberUpdateRequest.getOption())) {
            case MEMBERNAME-> this.name = value;
            //임베디드타입 새 객체로 변경해야함
            case ADDRESS -> {
                //서울 강남구 12345
                String[] parts = value.trim().split("\\s+");
                if (parts.length != 3) {
                    throw new InvaildArgumentException(ErrorCode.INVALID_INPUT);
                }
                String city = parts[0];
                String street = parts[1];
                String zipcode = parts[2];

                this.address = new Address(city, street, zipcode); //새객체
            }
            case PHONE -> this.phone = value;
        }
    }


    //회원 수정 옵션
    enum UpdateInfo {
        MEMBERNAME("membername"),
        ADDRESS("address"),
        PHONE("phone");

        private String option;
        UpdateInfo(String option) {
            this.option = option;
        }
        private static UpdateInfo getUpdateOption(String input) {
            return Arrays
                    .stream(UpdateInfo.values())
                    .filter(user -> user.option.equals(input))
                    .findFirst()
                    .orElseThrow(() -> new InvaildArgumentException(ErrorCode.INVALID_INPUT));
        }

    }











}
