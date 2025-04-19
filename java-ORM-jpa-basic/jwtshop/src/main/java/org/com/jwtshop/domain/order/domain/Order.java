package org.com.jwtshop.domain.order.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.member.domain.Member;
import org.com.jwtshop.domain.order.model.OrderStatus;
import org.com.jwtshop.global.domain.BaseEntity;

@Getter
@Entity
@Table(name="orders")

public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id 값 자동 증가
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    //주문 상태
    @Column(columnDefinition = "VARCHAR(45) default '주문진행중'")
    private String status = "주문 진행";

    //기본 생성자
    protected Order(){

    }
    @Builder
    private Order(
            Long orderId,
            Member member


    ){
        this.orderId = orderId;
        this.member = member;
    }
    //주문 상태 변경
    public void changeStatus(OrderStatus status){
        this.status = status.getStatus();
    }
    //취소하는 경우
    public void cancle() {
        this.status = OrderStatus.ORDER_CANCELED.getStatus();
    }

}
