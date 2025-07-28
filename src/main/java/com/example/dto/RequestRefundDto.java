package com.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRefundDto {

    private int id;

    private int buyerId;

    @NotNull(message = "El ID del pago es necesario")
    private int paymentId;

    @NotNull(message = "El estado de confirmación es necesario, 0: pendiente, 1: aprobado, 2: rechazado")
    @Min(value = 0, message = "El valor de confirmación debe estar entre 0 y 2")
    @Max(value = 2, message = "El valor de confirmación debe estar entre 0 y 2")
    private int confirmation;

    @NotNull(message = "El tipo de reembolso es obligatorio")
    @Min(value = 0, message = "El tipo de reembolso debe ser mayor o igual a 0")
    private int refundType;

    private String reason;

}
