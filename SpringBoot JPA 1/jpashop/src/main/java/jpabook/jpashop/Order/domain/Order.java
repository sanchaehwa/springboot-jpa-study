package jpabook.jpashop.Order.domain;

import jakarta.persistence.*;
import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Order.model.OrderStatus;
import lombok.AccessLevel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL) //부모클래스가 삭제되면 자식클래스도 소멸
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태



    @Builder
    public Order(Member member,  Delivery delivery, OrderStatus status) {
        this.member = member;
        this.delivery = delivery;
        this.orderDate = LocalDateTime.now();
        this.status = status;
    }

    //편의 매서드
    public void setMember(Member member) {
        this.member = member;
    }
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.addOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.addOrder(this);
    }





}
