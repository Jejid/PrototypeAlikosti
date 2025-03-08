package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "request_refund")
public class RequestRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "El ID del comprador es necesario")
    private int buyerId;

    @NotNull(message = "El ID del pago es necesario")
    private int paymentId;

    @NotNull(message = "El estado de confirmación es necesario")
    @Min(value = 0, message = "El valor de confirmación debe estar entre 0 y 2")
    @Max(value = 2, message = "El valor de confirmación debe estar entre 0 y 2")
    private int confirmation;

    @NotNull(message = "El tipo de reembolso es obligatorio")
    @Min(value = 0, message = "El tipo de reembolso debe ser mayor o igual a 0")
    private int refundType;
}
