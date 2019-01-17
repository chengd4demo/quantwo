package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.AccountInBound;

@Repository
public interface AccountInBoundRepository extends JpaRepository<AccountInBound, String> {

}
