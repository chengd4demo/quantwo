package com.qt.air.cleaner.market.repository.price;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.price.PriceSystem;

@Repository
public interface PriceSystemRepository extends JpaRepository<PriceSystem, String>{
	List<PriceSystem> findByRemovedAndState(Boolean removed,Integer state);
	Page<PriceSystem> findAll(Specification<PriceSystem> specification, Pageable pageable);
	PriceSystem findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update PriceSystem I set I.removed = true where I.id = :id")
	void deletePriceSystem(@Param("id") String id);
	@Modifying
	@Query("update PriceSystem I set I.state = :state where I.id = :id")
	void updateState(@Param("id") String id, @Param("state") Integer state);
	

}
