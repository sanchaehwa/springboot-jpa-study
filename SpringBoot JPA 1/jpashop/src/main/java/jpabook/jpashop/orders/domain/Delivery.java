package jpabook.jpashop.Order.domain;

import jakarta.persistence.*;
import jpabook.jpashop.Member.domain.Address;
import jpabook.jpashop.Order.model.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery")

public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Column(nullable = false)
    private DeliveryStatus status;

    @Embedded
    private Address address;


    @Builder
    public Delivery(Order order, DeliveryStatus status, Address address) {
        this.order = order;
        this.status = status;
        this.address = address;
    }

    public void setOrder(Order order){
        this.order = order;
    }
}
