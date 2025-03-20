package com.example.dto;

import jakarta.persistence.*;
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
    private String name;
    private int price;
    private String description;
    private int stock;
    private String pic;

}
