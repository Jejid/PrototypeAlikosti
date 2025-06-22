package com.example.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String name;

    private String description;
}
