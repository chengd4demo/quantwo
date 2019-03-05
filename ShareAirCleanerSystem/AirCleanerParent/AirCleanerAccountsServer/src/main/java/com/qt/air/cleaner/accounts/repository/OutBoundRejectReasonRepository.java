package com.qt.air.cleaner.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.accounts.domain.OutBoundRejectReason;

@Repository
public interface OutBoundRejectReasonRepository extends JpaRepository<OutBoundRejectReason, String> {

}
