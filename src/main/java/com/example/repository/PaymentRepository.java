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

}
