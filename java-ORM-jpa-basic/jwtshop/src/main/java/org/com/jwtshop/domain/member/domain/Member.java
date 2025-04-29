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
import org.com.jwtshop.domain.order.domain.Order;

import java.util.*;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class Member  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //member id 자동 증가
    private Long member_id;

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

    public void addOrders(Order orders){
        this.orders.add(orders);
    }


    @Builder
    private Member(Long member_id, String name, Address address, String phone) {
        this.member_id = member_id;
        this.name = name;
        this.address = address;
        this.phone = phone;

    }





}
