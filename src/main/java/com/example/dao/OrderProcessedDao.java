package com.example.dao;

import com.example.key.OrderProcessedKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(OrderProcessedKey.class)
@Table(name = "order_processed")
public class OrderProcessedDao {

    @Id
    @Column(name = "payment_id")
    @NotNull(message = "paymentId es necesario")
    private Integer paymentId;

    @Id
    @Column(name = "product_id")
    @NotNull(message = "productId es necesario")
    private Integer productId;

    @Column(name = "units")
    @Min(value = 1, message = "Debe haber al menos una unidad")
    private int units;

    @Column(name = "total_product")
    @Min(value = 0, message = "El total no puede ser negativo")
    private int total_product;
}