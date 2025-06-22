package com.example.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payment")
public class PaymentDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int buyerId;

    private int paymentMethodId;

    private int totalOrder;

    private String date;

    private int confirmation;

    private Integer codeConfirmation;

    private String cardNumber;

    private boolean refunded = false;
}
