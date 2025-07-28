package com.example.dto.paygate;

import lombok.Data;

@Data
public class CreditCardToken {
    private String payerId;
    private String name;
    private String identificationNumber;
    private String paymentMethod; // Ej: VISA, MASTERCARD, etc.
    private String number; // Número real de la tarjeta
    private String expirationDate; // Formato: yyyy/MM
}
