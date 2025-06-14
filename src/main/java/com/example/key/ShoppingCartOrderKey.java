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

    //constructor vacio
    public ShoppingCartOrderKey() {
    }

    // Constructor con campos
    public ShoppingCartOrderKey(int buyerId, int productId) {
        this.buyerId = buyerId;
        this.productId = productId;
    }


    // equals y hashCode necesarios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCartOrderKey that)) return false;
        return buyerId == that.buyerId && productId == that.productId;
    }

    // verifica el codigo unico de la combinación, asi nos aseguramos que esté en el mismo bucket
    @Override
    public int hashCode() {
        return Objects.hash(buyerId, productId);
    }
}
