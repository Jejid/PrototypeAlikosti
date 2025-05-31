package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRefundDto {

    //private int id;
    private int buyerId;
    private int paymentId;
    private int confirmation;
    private int refundType;

}
