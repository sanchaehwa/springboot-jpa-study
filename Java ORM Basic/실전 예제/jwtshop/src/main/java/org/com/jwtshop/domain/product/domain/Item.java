package org.com.jwtshop.domain.product.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="item")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@DiscriminatorColumn(name ="dtype")

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long item_id;

    @Column(nullable=false)
    private String item_name;

    @Column(nullable=false)
    private int price;

    @Column(nullable = false)
    private int stock_quantity;


    @OneToMany(mappedBy = "item")
    private List<ItemCategory> itemCategories = new ArrayList<>();



    public Item(
            String item_name,
            int price,
            int stock_quantity
    ) {
        this.item_name = item_name;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }





}
