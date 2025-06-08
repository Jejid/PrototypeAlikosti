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


}
