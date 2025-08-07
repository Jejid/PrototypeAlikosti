package com.example.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuyerDto {

    private int id;

    @NotBlank(message = "El nombre es necesario")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String surname;

    private String birthDate;

    private String cc;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String passAccount;

    private String phone;
}