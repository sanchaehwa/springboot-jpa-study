package jpabook.jpashop.Member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jpabook.jpashop.orders.domain.Order;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//필드 초기화
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private Long id;

    //유효성 검사
    @Column(nullable = false)
    private String username;

    @Embedded
    private Address address;

    //주문
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


    @Builder
    public Member(String username, Address address) {
        this.username = username;
        this.address = address;

    }
}
