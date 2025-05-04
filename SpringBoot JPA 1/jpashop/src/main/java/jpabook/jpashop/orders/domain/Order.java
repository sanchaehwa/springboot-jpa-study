package jpabook.jpashop.orders.domain;

import jakarta.persistence.*;
import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.orders.model.DeliveryStatus;
import jpabook.jpashop.orders.model.OrderStatus;
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
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //cascade 설정해주면 부모엔티티를 저장시 자식 엔티티도 같이 Persist 해주는것.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //부모클래스가 삭제되면 자식클래스도 소멸
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태

    @Builder
    public Order(Member member, Delivery delivery, OrderStatus status) {
        this.member = member;
        this.delivery = delivery;
        this.status = status;
        this.orderDate = LocalDateTime.now();
    }


    // 생성 매서드 = 정적 팩토리 매서드 빌더패턴을 내부에 쓰긴하지만, 연관관계 편의 매서드 까지 호출함으로써 완성된 Order 만들어줌.

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .status(OrderStatus.ORDERED)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }


    //편의 매서드
    public void assignMember(Member member) {
        this.member = member;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.addOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        //순환 호출 방지 -> 단순히 자기 자신만 세팅하는 순환 호출을 발생시키지 않게함
        if (delivery.getOrder() != this) {
            delivery.setOrder(this);
        }
    }

    //비즈니스 로직
    public void cancelOrder() {
        if (delivery.getStatus() == DeliveryStatus.DELIVERY_COMPLETED) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능 합니다");
        }
        this.status = OrderStatus.ORDER_CANCELED;
        //재고원복(취소했으니깐 그만큼 수량 증가)
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //조회 로직 - 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        //스트림 표현 가능 :컬랙션에 저장된 데이터를 함수형 스타일로
        // return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}






