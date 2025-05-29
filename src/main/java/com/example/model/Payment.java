package com.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Payment {

    private int id;
    private int buyerId;
    private int paymentMethodId;
    private int totalOrder;
    private String date;
    private int confirmation;
    private Integer codeConfirmation;
    private String cardNumber;
    private boolean refunded;

    public Payment() {
    }

    public Payment(int id, int buyerId, int paymentMethodId, int totalOrder, String date,
                   int confirmation, Integer codeConfirmation, String cardNumber, boolean refunded) {
        this.id = id;
        this.buyerId = buyerId;
        this.paymentMethodId = paymentMethodId;
        this.totalOrder = totalOrder;
        this.date = date;
        this.confirmation = confirmation;
        this.codeConfirmation = codeConfirmation;
        this.cardNumber = cardNumber;
        this.refunded = refunded;
    }
}