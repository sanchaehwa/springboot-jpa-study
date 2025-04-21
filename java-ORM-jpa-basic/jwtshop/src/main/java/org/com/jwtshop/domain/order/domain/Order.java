package org.com.jwtshop.domain.order.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.member.domain.Member;
import org.com.jwtshop.domain.order.model.OrderStatus;
import org.com.jwtshop.domain.product.domain.OrderItem;
import org.com.jwtshop.global.domain.BaseEntity;
import java.util.*;

@Getter
@Entity
@Table(name="orders")

public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id 값 자동 증가
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //기본 생성자
    protected Order(){

    }

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(int price, int count) {
        this.orderItems.add(buildOrderItem(price, count));
    }

    @Builder
    private Order(
            Long orderId,
            Member member,
            OrderStatus status

    ){
        this.orderId = orderId;
        this.member = member;
        this.status = status;
    }


    private OrderItem buildOrderItem(int price, int count) {
        return OrderItem
                .builder()
                .order(this)   // 양방향 연관관계 주입
                .orderPrice(price)
                .count(count)
                .build();
    }


}
