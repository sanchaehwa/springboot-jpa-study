package org.com.jwtshop.domain.product.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter

public class ItemCategory {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id")
    private Category category;
}
