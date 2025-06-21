package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreditCardDto {


    //private int id;

    //@NotNull(message = "El ID del comprador es necesario")
    //private int buyerId;

    @Column(name = "name", length = 20, nullable = false)
    @NotBlank(message = "El nombre del producto es necesario")
    private String name;

    //@Column(name = "card_number", length = 35, nullable = false)
    //@NotBlank(message = "El número de tarjeta es necesario")
    //private String cardNumber;

    //@NotBlank(message = "La fecha de vencimiento de la tarjeta es necesario")
    //private String cardDate;

    //@NotNull(message = "El código CVC de la tarjeta es necesario")
    //private int cvcCode;

    //private String tokenizedCode;

    @NotBlank(message = "El nombre del banco de la tarjeta es necesario")
    private String bank;
}
