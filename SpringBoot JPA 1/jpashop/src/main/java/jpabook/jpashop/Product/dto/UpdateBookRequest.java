package jpabook.jpashop.Product.dto;


import lombok.Getter;

@Getter

public class UpdateBookRequest {

    private final String name;
    private final int price;
    private final int stockQuantity;

    public UpdateBookRequest(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
