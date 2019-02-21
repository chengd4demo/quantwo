package com.qt.air.cleaner.market.repository.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.qt.air.cleaner.market.domain.activity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
			
	Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
		
}
