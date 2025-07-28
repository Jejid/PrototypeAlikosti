package com.example.dto.paygate;

import lombok.Data;

@Data
public class BuyerPayGate {
    private String fullName;
    private String emailAddress;
    private String dniNumber;
    private String contactPhone;
    private ShippingAddress shippingAddress;
}
