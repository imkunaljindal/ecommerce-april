package com.example.ecommerce.repository;

import com.example.ecommerce.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface CardRespository extends JpaRepository<Card,Integer> {

    Card findByCardNo(String cardNo);
}
