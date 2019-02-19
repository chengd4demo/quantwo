package com.qt.air.cleaner.market.repository.account;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.AccountOutBound;

@Repository
public interface AccountOutBoundRepository extends JpaRepository<AccountOutBound, String>{

	Page<AccountOutBound> findAll(Specification<AccountOutBound> specification, Pageable pageable);
	AccountOutBound findByIdAndRemoved(String id, Boolean removed);
	@Query("select count(t.id) from AccountOutBound t where (t.createTime between :start and :end) and t.removed = false")
	Long findCount(@Param("start") Date start, @Param("end") Date end);
}
