package com.qt.air.cleaner.scheduled.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {
	Billing findByBillingId(String billingId);
	@Query(value="select t.mach_no as machNo,to_char(t.create_time,'YYYY-MM-DD'),count(t.id) as total from act_billing t group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc",nativeQuery = true)
	List<Billing> findByAll();
}
