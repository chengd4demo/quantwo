package com.qt.air.cleaner.market.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qt.air.cleaner.market.domain.account.BillingRefundReason;

public interface BillingRefundReasonRepository extends JpaRepository<BillingRefundReason, String> {

}
