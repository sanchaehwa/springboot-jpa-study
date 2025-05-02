package org.com.jwtshop.domain.order.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.order.model.DeliveryStatus;
import org.com.jwtshop.domain.member.domain.Address;
import org.com.jwtshop.global.domain.BaseEntity;

@Entity
@Getter
@Table(name="delivery")
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long delivery_id;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "delivery")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Embedded
    private Address address;
    //기본생성자
    protected Delivery() {}

    @Builder
    public Delivery(Order order, DeliveryStatus status, Address address) {
        this.order = order;
        this.status = status;
        this.address = address;
    }

    public void addOrder(Order order) {
        this.order = order;
    }


}
