package jpabook.jpashop.orders.model;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    DELIVERY_READY("배송 준비"),
    DELIVERY_COMPLETED("배송 완료")
    ;
    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

}
