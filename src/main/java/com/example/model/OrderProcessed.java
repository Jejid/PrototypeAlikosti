package com.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderProcessed {

    private int paymentId;
    private int productId;
    private int units;
    private int totalProduct;

}
