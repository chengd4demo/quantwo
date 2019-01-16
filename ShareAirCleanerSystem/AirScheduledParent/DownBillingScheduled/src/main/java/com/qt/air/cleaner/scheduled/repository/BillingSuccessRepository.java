package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.BillingSuccess;

@Repository
public interface BillingSuccessRepository extends JpaRepository<BillingSuccess, String> {
	BillingSuccess findByTransactionId(String transactionId);
}
