package com.qt.air.cleaner.market.repository.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.BillingRefund;

@Repository
public interface BillingRefundRepository extends JpaRepository<BillingRefund, String>{
	Page<BillingRefund> findAll(Specification<BillingRefund> specification,Pageable pageable);
	BillingRefund findByIdAndRemoved(String id, boolean removed);
}
