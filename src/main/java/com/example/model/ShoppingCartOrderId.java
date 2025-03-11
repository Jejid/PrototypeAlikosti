package com.example.model;

import java.io.Serializable;
import java.util.Objects;

public class ShoppingCartOrderId implements Serializable {
    private Integer buyer;
    private Integer product;

    public ShoppingCartOrderId() {}

    public ShoppingCartOrderId(Integer buyer, Integer product) {
        this.buyer = buyer;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartOrderId that = (ShoppingCartOrderId) o;
        return Objects.equals(buyer, that.buyer) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyer, product);
    }
}
