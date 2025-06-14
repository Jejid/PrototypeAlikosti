package com.example.repository;

import com.example.dao.BuyerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<BuyerDao, Integer> {

    // @Query("Select A from buyer")
    //void allFromBuyer ();

}