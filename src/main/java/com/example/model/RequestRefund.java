package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRefund {
    private int id;
    private int buyerId;
    private int paymentId;
    private int confirmation;
    private int refundType;
    private String reason;
}
