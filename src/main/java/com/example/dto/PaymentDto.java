package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    private boolean refunded = false;
}
