package com.example.dao;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "buyer") // Asegurar que coincide con la tabla en PostgreSQL
public class BuyerDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;
    private String surname;


    private String birthDate;

    //cedula de ciudadania
    private String cc;

    private String email;

    private String passAccount;
}