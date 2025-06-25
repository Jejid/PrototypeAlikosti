package com.example.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreditCard {

    private int id;
    private int buyerId;
    private String name;
    private String cardNumber;
    private String cardDate;
    private int cvcCode;
    private String tokenizedCode;
    private String bank;


    public CreditCard(int id, int buyerId, String name, String cardNumber, String cardDate, int cvcCode, String tokenizedCode, String bank) {
        this.id = id;
        this.buyerId = buyerId;
        this.name = name;
        this.cardNumber = cardNumber;
        this.cardDate = cardDate;
        this.cvcCode = cvcCode;
        this.tokenizedCode = tokenizedCode;
        this.bank = bank;
    }
}
