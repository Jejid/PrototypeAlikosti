package com.example.repository;

import com.example.dao.PaymentDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDao, Integer> {

    boolean existsByBuyerId(Integer buyerId);

    boolean existsByPaymentMethodId(Integer paymentMethodId);

    @Query(value = """
            SELECT *
            FROM payment
            WHERE buyer_id = :buyerId
            """, nativeQuery = true)
    List<PaymentDao> findPaymentsByBuyerId(@Param("buyerId") int buyerId);

    @Query(value = """
            SELECT
                p.buyer_id AS buyerId,
                b.name AS nameBuyer,
                SUM(p.total_order) AS totalSales
            FROM payment p
            JOIN buyer b ON p.buyer_id = b.id
            WHERE p.confirmation = 1 AND p.refunded = false
            GROUP BY p.buyer_id, b.name
            """, nativeQuery = true)
    List<Object[]> findSalesReportList();

}
