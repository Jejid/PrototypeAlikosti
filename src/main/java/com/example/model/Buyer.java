package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "buyer")
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 10, nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Column(name = "surname", length = 10, nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    private String surname;

    @Column(name = "birth_date", length = 15)
    private String birthDate;

    //cedula de ciudadania
    @Column(name = "cc", length = 12)
    private String cc;

    @Column(name = "email", length = 30, nullable = false, unique = true)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String email;

    @Column(name = "pass_account", length = 20, nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    private String passAccount;
}
