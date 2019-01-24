package com.qt.air.cleaner.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.accounts.domain.Trader;

@Repository
public interface TraderRepository extends JpaRepository<Trader, String> {
	Trader findByWeixin(String weixin);
}
