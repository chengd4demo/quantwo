package com.qt.air.cleaner.market.repository.account;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.BillingRefund;

@Repository
public interface BillingRefundRepository extends JpaRepository<BillingRefund, String>{
	Page<BillingRefund> findAll(Specification<BillingRefund> specification,Pageable pageable);
	BillingRefund findByIdAndRemoved(String id, boolean removed);
	@Query("select count(t.id) from BillingRefund t where (t.createTime between :start and :end) and t.removed = false")
	Long findCount(@Param("start") Date start, @Param("end") Date end);
}
