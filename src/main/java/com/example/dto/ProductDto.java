package com.example.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {

    //private int id;
    private int categoryId;
    private String name;
    private int price;
    private String description;
    private int stock;
    private String pic;
    //private int storeId;
}
