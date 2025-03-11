package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "shoppingcart_order")
@IdClass(ShoppingCartOrderId.class)
public class ShoppingCartOrder {

    @Id
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Las unidades son obligatorias")
    @Min(value = 0, message = "Las unidades deben ser mayores o iguales a 0")
    private int units;

    @NotNull(message = "El total es obligatorio")
    @Min(value = 0, message = "El total debe ser mayor o igual a 0")
    private int total;
}