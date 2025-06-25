package com.example.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "credit_card") // Asegurar que coincide con la tabla en PostgreSQL
public class CreditCardDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int buyerId;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "card_number", length = 35, nullable = false)
    private String cardNumber;

    private String cardDate;

    private int cvcCode;

    private String tokenizedCode;

    private String bank;
}
