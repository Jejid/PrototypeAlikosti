package com.example.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {

    private int id;

    @NotNull(message = "La categoria no puede estar vacía")
    private int categoryId;

    @NotBlank(message = "El nombre del producto es necesario")
    private String name;

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private int price;

    private String description;

    @NotNull(message = "El stock no puede estar vacío")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private int stock;


    private String pic;

    @NotNull(message = "El ID de la tienda es necesario")
    private int storeId;
}
