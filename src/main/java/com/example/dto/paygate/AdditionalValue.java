package com.example.dto.paygate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdditionalValue {

    @JsonProperty("TX_VALUE")
    private Amount TX_VALUE;

    @JsonProperty("TX_TAX")
    private Amount TX_TAX;

    @Data
    public static class Amount {
        private String value;
        private String currency = "COP";
    }
}
