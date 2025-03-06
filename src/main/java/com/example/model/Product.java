package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product") // Asegurar que coincide con la tabla en PostgreSQL
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int category_id;
    private int store_id;
    @Column(name = "name", length = 15, nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private int price;
    @Column(name = "description", length = 40)
    private String description;
    private int stock;
    @Column(name = "pic", length = 50)
    private String pic;

    //Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getCategory_id() {return category_id;}
    public void setCategory_id(int category_id) {this.category_id = category_id;}

    public int getStore_id() {return store_id;}
    public void setStore_id(int store_id) {this.store_id = store_id;}

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
