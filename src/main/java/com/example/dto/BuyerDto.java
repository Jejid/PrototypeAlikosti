
package com.example.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BuyerDto {

    //private int id; MOSTRARLO¡

    @Column(name = "name", length = 10, nullable = false)
    @NotBlank(message = "El nombre es necesario")
    private String name;

    @Column(name = "surname", length = 10, nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    private String surname;

    @Column(name = "birth_date", length = 15)
    private String birthDate;

    //@Column(name = "cc", length = 12)
    //private String cc;

    @Column(name = "email", length = 30, nullable = false, unique = true)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String email;

    //@Column(name = "pass_account", length = 20, nullable = false)
    //@NotBlank(message = "La contraseña es obligatoria")
    //private String passAccount;
}