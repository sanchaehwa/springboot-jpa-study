package org.com.jwtshop.domain.order.model;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    DELIVERY_EXPECTED("배송 예정"),
    DELIVERY("배송 진행 중"),
    DELIVERY_CANCELED("배송 취소"),
    DELIVERY_COMPLETED("배송 완료"),
    DELIVERY_DELAY("배송 지연");
    ;
    private final String status;

    DeliveryStatus(String status) {this.status = status;}
}
