package com.example.dto.payu;

import lombok.Data;

@Data
public class PayuPaymentRequest {
    private String language = "es";
    private String command = "SUBMIT_TRANSACTION";
    private boolean test = true;
    private Merchant merchant;
    private Transaction transaction;
}
