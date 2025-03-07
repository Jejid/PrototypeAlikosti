package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product") // Asegurar que coincide con la tabla en PostgreSQL
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int categoryId;
    private int storeId;
    @Column(name = "name", length = 30, nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private int price;
    private String description;
    private int stock;
    private String pic;

    //Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getCategoryId() {return categoryId;}
    public void setCategoryId(int categoryId) {this.categoryId = categoryId;}

    public int getStoreId() {return storeId;}
    public void setStoreId(int storeId) {this.storeId = storeId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}

    public String getPic() {return pic;}
    public void setPic(String pic) {this.pic = pic;}
}
