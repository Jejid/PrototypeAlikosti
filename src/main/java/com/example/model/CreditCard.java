package com.example.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    private int id;
    private int buyerId;
    private String name;
    private String cardNumber;
    private String cardDate;
    private String cvcCode;
    private String tokenizedCode;
    private String bank;
    private String cardType;
    private String franchise;

}
