package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessedDto {

    @NotNull(message = "paymentId es necesario")
    private int paymentId;

    @NotNull(message = "productId es necesario")
    private int productId;

    @Min(value = 1, message = "Debe haber al menos una unidad")
    private int units;

    @Min(value = 0, message = "El total no puede ser negativo")
    private int total_product;
}