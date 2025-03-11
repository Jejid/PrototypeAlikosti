package com.example.repository;

import com.example.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
}