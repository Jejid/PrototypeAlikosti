package com.example.repository;

import com.example.model.RequestRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRefundRepository extends JpaRepository<RequestRefund, Integer> {
}
