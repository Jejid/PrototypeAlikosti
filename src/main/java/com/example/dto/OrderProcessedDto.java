package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessedDto {
    private int paymentId;
    private int productId;
    private int units;
    private int total_product;
}