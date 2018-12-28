package com.qt.air.cleaner.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.order.domain.Trader;

@Repository
public interface TraderRepository extends JpaRepository<Trader, String> {
	Trader findByWeixinAndRemoved(String weixin,Boolean removed);
	@Query(value="select t.address from Trader t ,Device d,Investor i where i.id = d.investor.id and t.id = d.trader.id and d.removed = false and i.weixin = :weixin and d.id = :deviceId")
	String findByWeixinAndDeviceId(@Param("weixin") String weixin,@Param("deviceId") String deviceId);
}
