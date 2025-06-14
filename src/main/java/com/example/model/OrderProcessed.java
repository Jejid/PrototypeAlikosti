package com.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessed {

    private int paymentId;
    private int productId;
    private int units;
    private int total_product;

   public OrderProcessed(){}
}
