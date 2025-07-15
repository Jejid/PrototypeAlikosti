package com.example.dto.payu;

import lombok.Data;

@Data
public class Order {
    private String id; // ✅ necesario para reembolsos
    private String accountId;
    private String referenceCode;
    private String description;
    private String language = "es";
    private String signature;
    private Buyer buyer;
    private AdditionalValue additionalValues;
}
