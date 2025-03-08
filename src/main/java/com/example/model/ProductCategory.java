package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "product_category") // Asegurar que coincide con la tabla en PostgreSQL
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "El ID de la tienda es obligatorio")
    private int storeId;

    @Column(name = "name", length = 30, nullable = false)
    @NotBlank(message = "El nombre de la categoría de producto es obligatorio")
    private String name;

    @Column(name = "description", length = 50)
    private String description;

}
