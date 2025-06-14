package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentDto {

    //private int id;
    private int buyerId;
    private int paymentMethodId;
    private int totalOrder;
    private String date;
    private int confirmation;
    private Integer codeConfirmation;
    private String cardNumber;
    private boolean refunded;
}
