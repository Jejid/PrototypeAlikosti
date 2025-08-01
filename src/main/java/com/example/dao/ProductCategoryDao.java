package com.example.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product_category") // Asegurar que coincide con la tabla en PostgreSQL
public class ProductCategoryDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int storeId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    private String description;
}
