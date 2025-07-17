package com.example.dto.payu;

import lombok.Data;

@Data
public class PayuTokenRequest {
    private String language;
    private String command;
    private Merchant merchant;
    private CreditCardToken creditCardToken;
}