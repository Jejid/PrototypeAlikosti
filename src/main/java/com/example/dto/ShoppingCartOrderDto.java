package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartOrderDto {
    private int buyerId;
    private int productId;
    private int units;
    private int total;
}
