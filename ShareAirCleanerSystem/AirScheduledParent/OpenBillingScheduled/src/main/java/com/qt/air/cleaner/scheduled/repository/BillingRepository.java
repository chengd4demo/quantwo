package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {
	Billing findByBillingId(String billingId);
}
