package com.qt.air.cleaner.market.repository.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.AccountOutBound;

@Repository
public interface AccountOutBoundRepository extends JpaRepository<AccountOutBound, String>{

	Page<AccountOutBound> findAll(Specification<AccountOutBound> specification, Pageable pageable);
	AccountOutBound findByIdAndRemoved(String id, boolean removed);
}
