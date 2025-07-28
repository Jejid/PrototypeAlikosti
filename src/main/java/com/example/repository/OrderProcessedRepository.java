package com.example.repository;

import com.example.dao.OrderProcessedDao;
import com.example.key.OrderProcessedKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProcessedRepository extends JpaRepository<OrderProcessedDao, OrderProcessedKey> {

    boolean existsByProductId(Integer productId);

    boolean existsByPaymentId(Integer paymentId);

    @Query(value = """
            SELECT *
            FROM order_processed
            WHERE payment_id IN (
              SELECT id
              FROM payment
              WHERE buyer_id = :buyerId
             )
            """, nativeQuery = true)
    List<OrderProcessedDao> findAllByBuyerId(@Param("buyerId") int buyerId);


    @Query("SELECT SUM(s.totalProduct) FROM OrderProcessedDao s WHERE s.paymentId = :paymentId")
    Integer sumTotalProductsByPaymentId(@Param("paymentId") Integer paymentId);

    @Query("""
            SELECT SUM(o.totalProduct)
            FROM OrderProcessedDao o
            WHERE o.paymentId IN (
                SELECT p.id
                FROM PaymentDao p
                WHERE p.buyerId = :buyerId
            )
            """)
    Integer sumTotalProductsByBuyerId(@Param("buyerId") int buyerId);
}
