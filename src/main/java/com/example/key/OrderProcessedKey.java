package com.example.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class OrderProcessedKey implements Serializable {

    private int paymentId;
    private int productId;

    // Constructor vacío
    public OrderProcessedKey() {}

    // Constructor con campos
    public OrderProcessedKey(int buyerId, int productId) {
        this.paymentId = buyerId;
        this.productId = productId;
    }


    // equals y hashCode necesarios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProcessedKey that)) return false;
        return paymentId == that.paymentId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, productId);
    }
}