package com.ofss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.entity.CreditCard;

@Repository
public interface CreditCardRepository
        extends JpaRepository<CreditCard, String> {
	
	List<CreditCard> findAllByCustomerId(Long customerId);
}