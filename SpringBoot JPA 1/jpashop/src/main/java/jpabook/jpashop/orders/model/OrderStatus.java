package jpabook.jpashop.orders.model;

import lombok.Getter;

@Getter
// @RequiredArgsConstructor 와 같은 생성자 코드 생성 Lombok 에노테이션은 사용 불가 - Enum의 생성자는 명확히 정의
public enum OrderStatus {
    ORDERED("주문 진행 중"),
    ORDER_CANCELED("주문 취소"),
    ORDER_COMPLETED("주문 완료");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
