package com.example.dto.paygate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundTransaction {
    private Order order; // Solo necesitas el ID
    private String type = "REFUND"; // o "PARTIAL_REFUND"
    private String parentTransactionId;
    private String reason; // Opcional, pero recomendable
    private AdditionalValue additionalValues; // solo para reembolso parcial
}
