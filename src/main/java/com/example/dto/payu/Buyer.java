package com.example.dto.payu;

import lombok.Data;

@Data
public class Buyer {
    private String fullName;
    private String emailAddress;
    private String dniNumber;
    private String contactPhone;
    private ShippingAddress shippingAddress;
}
