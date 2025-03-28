package com.example.model;

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



    public Product() {
    }

    public Product(int id, int categoryId, @NotBlank(message = "El nombre del producto es necesario") String name, @NotNull(message = "El precio no puede estar vacío") @Min(value = 0, message = "El precio debe ser mayor o igual a 0") int price, String description, @NotNull(message = "El stock no puede estar vacío") @Min(value = 0, message = "El stock debe ser mayor o igual a 0") int stock, String pic, @NotNull(message = "El ID de la tienda es necesario") int storeId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.pic = pic;
        this.storeId = storeId;
    }
}
