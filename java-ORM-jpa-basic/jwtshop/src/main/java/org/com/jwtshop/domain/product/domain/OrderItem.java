package org.com.jwtshop.domain.product.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.order.domain.Order;

@Getter
@Entity
@Table(name="order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_item_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int orderPrice;

    @Column(nullable = false)
    private int count;

    @Builder
    private OrderItem(
            Long order_item_id,
            Order order,
            Item item,
            int orderPrice,
            int count
    ) {
        this.order_item_id = order_item_id;
        this.order = order;
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    protected OrderItem() {

    }

}
