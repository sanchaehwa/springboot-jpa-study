package org.com.jwtshop.domain.order.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED("주문 진행 중"),
    ORDER_CANCELED("주문 취소"),
    ORDER_COMPLETED("주문 완료")
    ;
    private final String status;

    OrderStatus(String status) {this.status = status;}
}
