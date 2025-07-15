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

    @Column(name = "buyer_id")
    private int buyerId;

    @Column(name = "payment_method_id")
    private int paymentMethodId;

    @Column(name = "total_order")
    private int totalOrder;

    @Column(length = 10, nullable = false)
    private String date;

    private int confirmation;

    private String codeConfirmation;

    @Column(name = "card_number", length = 20)
    private String cardNumber;

    @Column(nullable = false)
    private boolean refunded = false;

    @Column(name = "paymentgateway_order_id")
    private String paymentGatewayOrderId;

    @Column(name = "paymentgateway_transaction_id")
    private String paymentGatewayTransactionId;
}
