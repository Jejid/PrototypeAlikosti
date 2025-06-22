package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentMethodDto {

   //private int id;

    @Column(length = 20, nullable = false)
    @NotBlank(message = "El nombre de método de pago es necesario")
    private String name;

    private String description;
}
