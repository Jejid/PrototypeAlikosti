package com.example.dto.payu;

import lombok.Data;

@Data
public class CreditCardPayu {
    private String number;
    private String securityCode;
    private String expirationDate; // formato "YYYY/MM"
    private String name;
}
