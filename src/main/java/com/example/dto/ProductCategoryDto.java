package com.example.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategoryDto {

    private int id;

    @NotNull(message = "El ID de la tienda es necesario")
    private int storeId;

    @NotBlank(message = "El nombre de Categria del producto es necesario")
    private String name;

    private String description;
}
