package com.qt.air.cleaner.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.order.domain.AccountInBound;

@Repository
public interface AccountInBoundRepository extends JpaRepository<AccountInBound, String> {
	Page<AccountInBound> findAll(Specification<AccountInBound> specification, Pageable pageable);
}
