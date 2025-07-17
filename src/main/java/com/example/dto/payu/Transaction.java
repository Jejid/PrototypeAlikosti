package com.example.dto.payu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Transaction {
    private Order order;
    private Payer payer;

    //@JsonProperty("creditCard")
    private Object creditCard; // Puede ser CreditCardPayu o CreditCardTokenInfo

    private String type = "AUTHORIZATION_AND_CAPTURE";
    private String paymentMethod;
    private String paymentCountry = "CO";
    private String deviceSessionId;
    private String ipAddress;
    private String cookie;
    private String userAgent;
    private String creditCardTokenId;
}

