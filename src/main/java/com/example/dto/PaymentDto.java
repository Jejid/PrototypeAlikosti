package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotNull(message = "El total del pedido no puede estar vacío")
    @Min(value = 0, message = "El total del pedido debe ser mayor o igual a 0")
    private int totalOrder;

    @NotBlank(message = "La fecha de pago es obligatoria")
    private String date;

    @NotNull(message = "El estado de confirmación es obligatorio")
    @Min(value = 0, message = "El estado de confirmación debe ser 0, 1 o 2")
    @Max(value = 2, message = "El estado de confirmación debe ser 0, 1 o 2")
    private int confirmation;

    @Column(name = "code_confirmation")
    private Integer codeConfirmation;

    private String cardNumber;

    private boolean refunded = false;
}
