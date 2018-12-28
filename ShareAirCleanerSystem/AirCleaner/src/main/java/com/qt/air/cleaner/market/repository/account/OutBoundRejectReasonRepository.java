package com.qt.air.cleaner.market.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qt.air.cleaner.market.domain.account.OutBoundRejectReason;

public interface OutBoundRejectReasonRepository extends JpaRepository<OutBoundRejectReason, String>{

}
