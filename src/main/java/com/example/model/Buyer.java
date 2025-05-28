package com.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Buyer {

    private int id;
    private String name;
    private String surname;
    private String birthDate;
    private String cc;
    private String email;
    private String passAccount;

    public Buyer() {
    }

    public Buyer(int id, String name, String surname, String birthDate, String cc, String email, String passAccount) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.cc = cc;
        this.email = email;
        this.passAccount = passAccount;
    }
}
