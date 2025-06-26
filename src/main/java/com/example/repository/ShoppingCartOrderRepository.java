package com.example.repository;

import com.example.dao.ShoppingCartOrderDao;
import com.example.key.ShoppingCartOrderKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartOrderRepository extends JpaRepository<ShoppingCartOrderDao, ShoppingCartOrderKey> {

    // Aquí se puede agregar métodos personalizados si es necesario
    boolean existsByBuyerId(Integer buyerId);

    boolean existsByProductId(Integer productId);

    @Query("SELECT SUM(s.totalProduct) FROM ShoppingCartOrderDao s WHERE s.buyerId = :buyerId")
    Integer sumTotalProductsByBuyerId(@Param("buyerId") Integer buyerId);

}
