package com.example.repository;

import com.example.dao.RequestRefundDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRefundRepository extends JpaRepository<RequestRefundDao, Integer> {

    boolean existsByBuyerId(Integer buyerId);
}
