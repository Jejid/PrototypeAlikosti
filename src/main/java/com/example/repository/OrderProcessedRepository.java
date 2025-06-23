package com.example.repository;

import com.example.dao.OrderProcessedDao;
import com.example.key.OrderProcessedKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProcessedRepository extends JpaRepository<OrderProcessedDao, OrderProcessedKey> {
    boolean existsByProductId(Integer productId);
    // Aquí se puede agregar métodos personalizados si es necesario
}
