package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.AccountOutBound;

@Repository
public interface AccountOutboundRepository extends JpaRepository<AccountOutBound, String>,JpaSpecificationExecutor<AccountOutBound> {
	
}
