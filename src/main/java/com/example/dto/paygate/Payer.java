package com.example.dto.paygate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Payer {
    private String fullName;
    private String emailAddress;
    private String dniNumber;
    private String contactPhone;
    private BillingAddress billingAddress;
}
