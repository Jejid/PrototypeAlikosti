package com.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentMethod {

    private int id;
    private String name;
    private String description;

    public PaymentMethod(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
