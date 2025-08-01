package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {

    private int id;

    @NotNull(message = "El ID del comprador es necesario")
    private int buyerId;

    @NotNull(message = "El ID del método de pago es necesario")
    private int paymentMethodId;

    private int totalOrder;
    private String date;
    private int confirmation;
    private String codeConfirmation;

    private String cardNumber;
    private String cvcCode;
    private String tokenizedCode;
    private String cardDate;
    private String franchise;
    private String cardName;
    private String cardType;
    private String bank;


    private boolean refunded = false;

    private String paymentGatewayOrderId;
    private String paymentGatewayTransactionId;


}
