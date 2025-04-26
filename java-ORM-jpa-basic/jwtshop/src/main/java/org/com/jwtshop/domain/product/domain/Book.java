package org.com.jwtshop.domain.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name="book")
@NoArgsConstructor

public class Book extends Item{

    private String author;
    private String isbn;

    @Builder
    public Book (String item_name, int price, int stock_quantity, String author, String isbn) {
        super(item_name, price, stock_quantity);
        this.author = author;
        this.isbn = isbn;
    }

}
