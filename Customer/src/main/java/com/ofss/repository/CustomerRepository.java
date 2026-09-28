package com.ofss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	 boolean existsByEmail(String email);

	    boolean existsByPanNumber(String panNumber);
	}

