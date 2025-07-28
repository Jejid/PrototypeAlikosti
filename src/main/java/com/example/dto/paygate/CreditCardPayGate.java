package com.example.dto.paygate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreditCardPayGate {
    private String number;
    private String securityCode;
    private String expirationDate; // formato "YYYY/MM"
    private String name;
}
