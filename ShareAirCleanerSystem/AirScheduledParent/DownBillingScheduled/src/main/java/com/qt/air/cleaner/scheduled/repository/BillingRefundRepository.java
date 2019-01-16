package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.BillingRefund;

@Repository
public interface BillingRefundRepository extends JpaRepository<BillingRefund, String> {
	BillingRefund findByTransactionId(String transactionId);
}
