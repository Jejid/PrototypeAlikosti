package com.example.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "request_refund")
public class RequestRefundDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int buyerId;

    private int paymentId;

    private int confirmation;

    private int refundType;

}
