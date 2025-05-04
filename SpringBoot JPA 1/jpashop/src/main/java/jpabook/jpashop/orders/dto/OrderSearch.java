package jpabook.jpashop.orders.dto;

import jpabook.jpashop.orders.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;

    @Builder
    public OrderSearch(String memberName, OrderStatus orderStatus) {
        this.memberName = memberName;
        this.orderStatus = orderStatus;
    }

}
