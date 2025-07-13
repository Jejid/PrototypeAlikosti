package com.example.dto.payu;

import lombok.Data;

@Data
public class AdditionalValue {
    private Amount TX_VALUE;

    @Data
    public static class Amount {
        private String value;
        private String currency = "COP";
    }
}
