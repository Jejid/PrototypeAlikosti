package com.example.dao;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product") // Asegurar que coincide con la tabla en PostgreSQL
public class ProductDao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int categoryId;

    private String name;

    private int price;

    private String description;

    private int stock;

    private String pic;

    private int storeId;

}
