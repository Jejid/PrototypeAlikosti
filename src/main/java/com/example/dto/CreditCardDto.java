package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreditCardDto {


    private int id;

    @NotNull(message = "El ID del comprador es necesario")
    private int buyerId;

    @NotBlank(message = "El nombre del producto es necesario")
    private String name;

    @NotBlank(message = "El número de tarjeta es necesario")
    private String cardNumber;

    @NotBlank(message = "La fecha de vencimiento de la tarjeta es necesaria")
    private String cardDate;

    @NotNull(message = "El código CVC de la tarjeta es necesario")
    private int cvcCode;

    private String tokenizedCode;

    @NotBlank(message = "El nombre del banco de la tarjeta es necesario")
    private String bank;

    @NotBlank(message = "El tipo de la tarjeta es necesario (ahorros o credito)")
    private String cardType;

    @NotBlank(message = "La franquicia de la tarjeta es necesaria (Visa, master card, american express, etc)")
    private String franchise;
}
