package com.example.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategoryDto {

    //private int id;

    @NotNull(message = "El ID de la tienda es necesario")
    private int storeId;

    @Column(name = "name", length = 30, nullable = false)
    @NotBlank(message = "El nombre de Categria del producto es necesario")
    private String name;

    private String description;
}
