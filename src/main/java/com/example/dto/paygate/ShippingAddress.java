package com.example.dto.paygate;

import lombok.Data;

@Data
public class ShippingAddress {
    private String street1;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
}