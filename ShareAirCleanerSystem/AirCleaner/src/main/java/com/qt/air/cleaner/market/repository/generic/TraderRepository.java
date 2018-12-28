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

import com.qt.air.cleaner.market.domain.generic.Trader;

@Repository
public interface TraderRepository extends JpaRepository<Trader, String>{
	List<Trader> findByRemoved(Boolean removed);
	Page<Trader> findAll(Specification<Trader> specification, Pageable pageable);
	Trader findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update Trader I set I.removed = true where I.id = :id")
	void deleteTrader(@Param("id") String id);	
}
