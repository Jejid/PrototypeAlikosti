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
    private Integer paymentId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    private int units;

    private int total_product;
}