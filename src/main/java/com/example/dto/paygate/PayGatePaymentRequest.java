package com.example.dto.paygate;

import lombok.Data;

@Data
public class PayGatePaymentRequest {
    private String language = "es";
    private String command = "SUBMIT_TRANSACTION";
    private boolean test = true;
    private Merchant merchant;
    private Transaction transaction;
}
