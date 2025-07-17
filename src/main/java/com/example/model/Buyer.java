package com.example.model;

import com.example.dto.payu.ShippingAddress;
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
    private String phone;
    private ShippingAddress shippingAddress;

}
