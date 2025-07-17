package com.example.dto.payu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Transaction {
    private Order order;
    private Payer payer;

    @JsonProperty("creditCard")
    private CreditCardPayu creditCardPayu;

    private String type = "AUTHORIZATION_AND_CAPTURE"; // tipo de pago inmediato
    private String paymentMethod; // VISA, MASTERCARD, etc.
    private String paymentCountry = "CO";
    private String deviceSessionId;
    private String ipAddress;
    private String cookie;
    private String userAgent;
}
