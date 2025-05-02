package org.com.jwtshop.domain.order.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.member.domain.Member;
import org.com.jwtshop.domain.order.model.OrderStatus;
import org.com.jwtshop.domain.product.domain.Item;
import org.com.jwtshop.domain.product.domain.OrderItem;
import org.com.jwtshop.global.domain.BaseEntity;

import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@Table(name="orders")

public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id 값 자동 증가
    private Long order_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member; //한명의 사용자가 여러개의 주문

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();

    //한 주문 - 하나의 배송정보
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) //주문정보가 삭제가되면 연관된 Delivery 도 같이 삭제 - Casecade : 영속성 전이
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;


    //기본 생성자
    protected Order(){

    }


    @Builder
    private Order(
            Long order_id,
            Member member,
            OrderStatus status

    ){
        this.order_id = order_id;
        this.member = member;
        this.status = status;
    }

    public void addOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(this)
                .orderPrice(orderPrice)
                .count(count)
                .build();
        this.orderItems.add(orderItem);
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.addOrder(this);
    }




}
