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

    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @Column(name = "surname", length = 10, nullable = false)
    private String surname;

    @Column(name = "birth_date", length = 15)
    private String birthDate;

    @Column(name = "cc", length = 12)
    private String cc;

    @Column(name = "email", length = 30, nullable = false, unique = true)
    private String email;

    @Column(name = "pass_account", length = 20, nullable = false)
    private String passAccount;

    private String phone;
}