package com.example.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {

    //private int id;
    private int categoryId;

    @Column(name = "name", length = 30, nullable = false)
    @NotBlank(message = "El nombre del producto es necesario")
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private int price;

    private String description;

    @NotNull(message = "El stock no puede estar vacío")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private int stock;


    private String pic;

    //@NotNull(message = "El ID de la tienda es necesario")
    //private int storeId;
}
