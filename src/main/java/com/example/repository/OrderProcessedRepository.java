package com.example.repository;

import com.example.dao.OrderProcessedDao;
import com.example.key.OrderProcessedKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProcessedRepository extends JpaRepository<OrderProcessedDao, OrderProcessedKey> {
    // Aquí se puede agregar métodos personalizados si es necesario
}
