package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Buyer {

    private int id;
    private String name;
    private String surname;
    private String birthDate;
    private String cc;
    private String email;
    private String passAccount;

}
