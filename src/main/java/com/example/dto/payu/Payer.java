package com.example.dto.payu;

import lombok.Data;

@Data
public class Payer {
    private String fullName;
    private String emailAddress;
    private String dniNumber;
    private String contactPhone;
    private BillingAddress billingAddress;
}
