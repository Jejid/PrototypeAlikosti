package com.example.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String name;

    private String cardNumber;

    private String cardDate;

    private int cvcCode;

    private String tokenizedCode;

    private String bank;
}
