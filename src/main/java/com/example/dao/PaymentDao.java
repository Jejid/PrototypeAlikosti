package com.example.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(length = 20)
    private String cardNumber;

    @Column(nullable = false)
    private boolean refunded = false;
}
