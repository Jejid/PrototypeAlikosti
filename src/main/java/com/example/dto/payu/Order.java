package com.example.dto.payu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Order {
    private String id; // ✅ necesario para reembolsos
    private String accountId;
    private String referenceCode;
    private String description;
    private String language = "es";
    private String signature;

    @JsonProperty("buyer")
    private BuyerPayu buyerPayu;
    private AdditionalValue additionalValues;
}
