package com.qt.air.cleaner.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.pay.domain.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String>{
	Billing findByBillingId(String billingId);
	
	@Query(value="select coalesce(sum((t.cost_time - floor((((sysdate-to_date(to_char(t.operate_time,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss'))) * 24 * 60)))),0) as surplusconsttime" +
			" from act_billing t, mk_device d where d.mach_no = t.mach_no  and t.transaction_id is not null  and t.removed = 'N'" +
			" and (((sysdate-to_date(to_char(t.operate_time,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss'))) * 24 * 60) < t.cost_time" +
			" and t.mach_no = :machNo and t.billing_id!= :billingId",nativeQuery = true)
	int getSurplusConstTime(@Param("machNo") String machNo,@Param("billingId") String billingId);
}
