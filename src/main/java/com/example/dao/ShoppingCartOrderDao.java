package com.example.dao;

import com.example.key.ShoppingCartOrderKey;
import jakarta.persistence.*;
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
    private Integer buyerId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "units")
    private int units;

    @Column(name = "total_product")
    private int totalProduct;
}
