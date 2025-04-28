package org.com.jwtshop.domain.order.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.com.jwtshop.domain.order.model.DeliveryStatus;
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

    //기본생성자
    protected Delivery() {}

    @Builder
    public Delivery(Order order, DeliveryStatus status) {
        this.order = order;
        this.status = status;
    }

    public void addOrder(Order order) {
        this.order = order;
    }


}
