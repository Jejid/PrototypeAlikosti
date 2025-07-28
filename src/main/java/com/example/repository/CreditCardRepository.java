package com.example.repository;

import com.example.dao.CreditCardDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardDao, Integer> {
    List<CreditCardDao> findByCardNumber(String cardNumber);

    Optional<CreditCardDao> findByCardNumberAndBuyerId(String cardNumber, Integer buyerId);
}