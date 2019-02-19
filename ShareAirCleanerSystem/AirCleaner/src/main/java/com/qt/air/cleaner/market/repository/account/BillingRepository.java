package com.qt.air.cleaner.market.repository.account;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {
	List<Billing> findByRemoved(Boolean removed);
	Page<Billing> findAll(Specification<Billing> specification, Pageable pageable);
	Billing findByIdAndRemoved(String id,Boolean removed);
	@Query("select count(t.id) from Billing t where (t.createTime between :start and :end) and t.removed = false")
	Long findCount(@Param("start") Date start, @Param("end") Date end);
	@Query("select count(t.id) from Billing t where (t.createTime between :start and :end) and t.removed = false and t.transactionId is not null")
	Long findVolumeCount(@Param("start") Date start, @Param("end") Date end);
}
