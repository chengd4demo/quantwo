package com.qt.air.cleaner.market.repository.activity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.activity.PrizeItemConfig;

@Repository
public interface PrizeItemConfigRepository extends JpaRepository<PrizeItemConfig, String> {
	Page<PrizeItemConfig> findAll(Specification<PrizeItemConfig> specification, Pageable pageable);
	PrizeItemConfig findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update PrizeItemConfig I set I.removed = true where I.id = :id")
	void deletePrizeItemConfig(@Param("id") String id);
	List<PrizeItemConfig> findByRemoved(boolean b);
}
