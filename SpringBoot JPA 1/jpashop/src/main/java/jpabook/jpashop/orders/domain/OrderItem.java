package jpabook.jpashop.orders.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jpabook.jpashop.Product.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    @Min(0) //가격이 0원인 경우도 있으니깐(서비스특성상)
    private int orderPrice;

    @Column(nullable = false)
    @Min(1) //주문은 1개이상은 해야함.
    private int count;

    //생성자
    @Builder
    public OrderItem(Order order, Item item, int orderPrice, int count) {
        this.order = order;
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }
    //생성 매서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(orderPrice)
                .count(count)
                .build();
        // 재고 차감
        item.removeStock(count);
        return orderItem;

    }

    //편의 매서드
    public void addOrder(Order order) {
        this.order = order;
    }

    //주문취소시 원복
    public void cancel() {
        getItem().addStock(count);

    }
    //총합 조회로직
    public int getTotalPrice() {
        return orderPrice * count;
    }
}
