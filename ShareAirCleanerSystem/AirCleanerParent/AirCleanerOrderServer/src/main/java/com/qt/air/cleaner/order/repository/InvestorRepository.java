package com.qt.air.cleaner.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qt.air.cleaner.order.domain.Investor;

public interface InvestorRepository extends JpaRepository<Investor, String> {
	Investor findByWeixinAndRemoved(String weixin,Boolean removed);
}
