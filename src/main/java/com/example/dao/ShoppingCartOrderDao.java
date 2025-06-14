package com.example.dao;

import com.example.key.ShoppingCartOrderKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(ShoppingCartOrderKey.class)
@Table(name = "shoppingcart_order")
public class ShoppingCartOrderDao {

    @Id
    @Column(name = "buyer_id")
    @NotNull(message = "buyerId es obligatorio")
    private Integer buyerId;

    @Id
    @Column(name = "product_id")
    @NotNull(message = "productId es obligatorio")
    private Integer productId;

    @Column(name = "units")
    @Min(value = 1, message = "Debe haber al menos una unidad")
    private int units;

    @Column(name = "total_product")
    @Min(value = 0, message = "El total no puede ser negativo")
    private int total_product;
}
