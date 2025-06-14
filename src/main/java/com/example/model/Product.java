package com.example.model;

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

    public Product(int id, int categoryId, String name, int price, String description, int stock, String pic, int storeId) {
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
