package com.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategory {
    private int id;
    private int storeId;
    private String name;
    private String description;

    public ProductCategory(int id, int storeId,String name, String description) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.description = description;

    }
}
