package com.example.repository;

import com.example.dao.BuyerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<BuyerDao, Integer> {
    Optional<BuyerDao> findByEmail(String email);

}