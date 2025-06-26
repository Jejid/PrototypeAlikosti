package com.example.repository;

import com.example.dao.OrderProcessedDao;
import com.example.key.OrderProcessedKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProcessedRepository extends JpaRepository<OrderProcessedDao, OrderProcessedKey> {
    boolean existsByProductId(Integer productId);

    boolean existsByPaymentId(Integer paymentId);

    @Query("SELECT SUM(s.totalProduct) FROM OrderProcessedDao s WHERE s.paymentId = :paymentId")
    Integer sumTotalProductsByPaymentId(@Param("paymentId") Integer paymentId);
}
