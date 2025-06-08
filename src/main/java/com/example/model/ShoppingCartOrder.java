package com.example.model;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ShoppingCartOrder {

    private int buyerId;
    private int productId;
    private int units;
    private int total_product;

    public ShoppingCartOrder() {}

    public ShoppingCartOrder(int buyerId, int productId, int units, int total_product) {
        this.buyerId = buyerId;
        this.productId = productId;
        this.units = units;
        this.total_product = total_product;
    }


}
