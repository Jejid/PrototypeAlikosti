package com.example.repository;

import com.example.dao.ProductDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDao, Integer> {
    boolean existsByCategoryId(Integer categoryId);
}
