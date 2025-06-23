package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentMethodDto {

    private int id;

    @NotBlank(message = "El nombre de método de pago es necesario")
    private String name;

    private String description;
}
