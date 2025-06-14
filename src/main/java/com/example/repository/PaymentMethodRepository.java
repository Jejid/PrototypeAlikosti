package com.example.repository;


import com.example.dao.PaymentMethodDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodDao, Integer> {
}
