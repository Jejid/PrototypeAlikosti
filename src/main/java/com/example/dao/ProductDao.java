package com.example.dao;


import jakarta.persistence.*;
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

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    private String description;

    private int stock;

    private String pic;

    private int storeId;

}
