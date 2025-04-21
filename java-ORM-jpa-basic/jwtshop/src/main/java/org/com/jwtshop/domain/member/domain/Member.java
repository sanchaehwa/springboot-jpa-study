package org.com.jwtshop.domain.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.order.domain.Order;

import java.util.*;

@Getter
@Entity
@Table(name = "members")
public class Member  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //member id 자동 증가
    private Long memberId;

    @Column(nullable=false, unique=true, length=45)
    private String name;

    @Column(nullable=false)
    private String city;

    @Column(nullable=false)
    private String street;

    @Column(nullable = false)
    private String zipcode;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    protected Member() {

    }
    @Builder
    private Member(Long memberId, String name, String city, String street, String zipcode) {
        this.memberId = memberId;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }



}
