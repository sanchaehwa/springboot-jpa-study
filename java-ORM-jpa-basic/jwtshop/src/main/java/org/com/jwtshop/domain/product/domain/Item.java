package org.com.jwtshop.domain.product.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name="item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long item_id;

    @Column(nullable=false)
    private String item_name;

    @Column(nullable = false)
    private int stock_quantity;

    @Builder
    public Item(
            Long item_id,
            String item_name,
            int stock_quantity
    ) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.stock_quantity = stock_quantity;
    }
    protected Item() {

    }




}
