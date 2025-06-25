package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartOrder {

    private int buyerId;
    private int productId;
    private int units;
    private int total_product;
}
