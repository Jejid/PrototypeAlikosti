package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartOrderDto {

    @NotNull(message = "buyerId es obligatorio")
    private int buyerId;

    @NotNull(message = "productId es obligatorio")
    private int productId;

    @Min(value = 1, message = "Debe haber al menos una unidad")
    private int units;

    @Min(value = 0, message = "El total no puede ser negativo")
    private int total_product;
}
