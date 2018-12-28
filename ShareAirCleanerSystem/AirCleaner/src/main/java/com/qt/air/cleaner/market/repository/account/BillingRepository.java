package com.qt.air.cleaner.market.repository.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {
	List<Billing> findByRemoved(Boolean removed);
	Page<Billing> findAll(Specification<Billing> specification, Pageable pageable);
	Billing findByIdAndRemoved(String id,Boolean removed);
}
