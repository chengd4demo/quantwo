package com.qt.air.cleaner.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.accounts.domain.Investor;

@Repository
public interface InvestorService extends JpaRepository<Investor, String> {
	Investor findByWeixin(String weixin);
}
