package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private int id;
    private int buyerId;
    private int paymentMethodId;
    private int totalOrder;
    private String date;
    private int confirmation;
    private String codeConfirmation;
    private String cardNumber;
    private boolean refunded;
}