package com.example.dto.payu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreditCardPayu {
    private String number;
    private String securityCode;
    private String expirationDate; // formato "YYYY/MM"
    private String name;
}
