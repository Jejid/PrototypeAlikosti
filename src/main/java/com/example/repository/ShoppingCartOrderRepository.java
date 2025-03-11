package com.example.repository;

import com.example.model.ShoppingCartOrder;
import com.example.model.ShoppingCartOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartOrderRepository extends JpaRepository<ShoppingCartOrder, ShoppingCartOrderId> {
}
