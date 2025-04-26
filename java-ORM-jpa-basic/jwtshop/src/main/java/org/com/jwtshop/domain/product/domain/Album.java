package org.com.jwtshop.domain.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
@Entity
@NoArgsConstructor
@Table(name = "album")
@Getter
public class Album extends Item {
    private String artist;

    @Builder
    public Album(String item_name, int price, int stock_quantity,String artist) {
        super( item_name, price, stock_quantity);
        this.artist = artist;
    }
}
