package com.qt.air.cleaner.market.repository.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.generic.Investor;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {
	List<Investor> findByRemoved(Boolean removed);
	Page<Investor> findAll(Specification<Investor> specification, Pageable pageable);
	Investor findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update Investor I set I.removed = true where I.id = :id")
	void deleteInvestor(@Param("id") String id);
}
