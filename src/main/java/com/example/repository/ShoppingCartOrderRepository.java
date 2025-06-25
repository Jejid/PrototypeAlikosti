package com.example.repository;

import com.example.dao.ShoppingCartOrderDao;
import com.example.key.ShoppingCartOrderKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartOrderRepository extends JpaRepository<ShoppingCartOrderDao, ShoppingCartOrderKey> {

    // Aquí se puede agregar métodos personalizados si es necesario
    boolean existsByBuyerId(Integer buyerId);

    boolean existsByProductId(Integer productId);
}
