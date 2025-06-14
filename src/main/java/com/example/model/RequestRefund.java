package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRefund {


    private int id;
    private int buyerId;
    private int paymentId;
    private int confirmation;
    private int refundType;

    public RequestRefund() {
    }

    public RequestRefund(int id, int buyerId, int paymentId, int confirmation, int refundType) {
        this.id = id;
        this.buyerId = buyerId;
        this.paymentId = paymentId;
        this.confirmation = confirmation;
        this.refundType = refundType;
    }
}
