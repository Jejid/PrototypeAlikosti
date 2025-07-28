package com.example.dto.paygate;

import lombok.Data;

@Data
public class PayGateTokenRequest {
    private String language;
    private String command;
    private Merchant merchant;
    private CreditCardToken creditCardToken;
}