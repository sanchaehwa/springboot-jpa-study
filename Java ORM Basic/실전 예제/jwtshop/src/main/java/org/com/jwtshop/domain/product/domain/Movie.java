package org.com.jwtshop.domain.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "movie")
@NoArgsConstructor
public class Movie extends Item{


    private String director;
    private String actors;

    @Builder
    public Movie(String item_name, int price, int stock_quantity, String director, String actors) {
        //부모 클래스의 값을 넣어줘야함
        super(item_name,price,stock_quantity);
        this.director = director;
        this.actors = actors;
    }
}
