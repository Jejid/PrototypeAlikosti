package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {

    private int id;
    private int categoryId;
    private String name;
    private int price;
    private String description;
    private int stock;
    private String pic;
    private int storeId;

    public Product(int id, @NotBlank(message = "El nombre del producto es necesario") String name, String description, @NotNull(message = "El precio no puede estar vacío") @Min(value = 0, message = "El precio debe ser mayor o igual a 0") int price, @NotNull(message = "El stock no puede estar vacío") @Min(value = 0, message = "El stock debe ser mayor o igual a 0") int stock, String pic, int categoryId, @NotNull(message = "El ID de la tienda es necesario") int storeId) {
    }

    public Product() {
    }
}
