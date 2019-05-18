package com.qt.air.cleaner.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qt.air.cleaner.order.domain.Investor;

public interface InvestorRepository extends JpaRepository<Investor, String> {
	Investor findByWeixinAndRemoved(String weixin,Boolean removed);
	@Query(value="select t.address from Trader t ,Device d,Investor i where i.id = d.investor.id and t.id = d.trader.id and d.removed = false and i.weixin = :weixin and d.id = :deviceId")
	String findInvestorByWeixinAndDeviceIdInvestor(@Param("weixin") String weixin,@Param("deviceId") String deviceId);
	@Query(value="select t.address from Trader t ,Device d,Company c where c.id = d.company.id and t.id = d.trader.id and d.removed = false and c.weixin = :weixin and d.id = :deviceId")
	String findCompanyByWeixinAndDeviceId(@Param("weixin") String weixin,@Param("deviceId") String deviceId);
	@Query(value="select t.address from mk_trader t,mk_device d,(select a2.pid, a2.weixin from (select ft.pid, ft.agent_id,ag.weixin from mk_agent ag left join ps_share_profit ft on ag.id = ft.agent_id) a2) a "
			+ "where  a.pid = d.distribution_ratio and t.id =  d.trader_id and d.removed = 'N' and a.weixin = :weixin and d.id = :deviceId",nativeQuery=true)
	String findAgentByWeixinAndDeviceId(@Param("weixin") String weixin,@Param("deviceId") String deviceId);
}
