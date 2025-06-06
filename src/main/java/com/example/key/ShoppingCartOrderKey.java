package com.example.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class ShoppingCartOrderKey implements Serializable {

    private int buyerId;
    private int productId;

    // Constructor vacío
    public ShoppingCartOrderKey() {}

    // Constructor con campos
    public ShoppingCartOrderKey(int buyerId, int productId) {
        this.buyerId = buyerId;
        this.productId = productId;
    }


    // equals y hashCode necesarios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCartOrderKey)) return false;
        ShoppingCartOrderKey that = (ShoppingCartOrderKey) o;
        return buyerId == that.buyerId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerId, productId);
    }
}
