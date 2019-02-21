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
import com.qt.air.cleaner.market.domain.activity.PrizeConfig;

@Repository
public interface PrizeConfigRepository extends JpaRepository<PrizeConfig, String>{

	Page<PrizeConfig> findAll(Specification<PrizeConfig> specification, Pageable pageable);
	PrizeConfig findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update PrizeConfig I set I.removed = true where I.id = :id")
	void deletePrizeConfig(@Param("id") String id);
	List<PrizeConfig> findByRemoved(Boolean b);
	@Modifying
	@Query("update PrizeConfig I set I.state = :state where I.id = :id")
	void updateState(@Param("id") String id, @Param("state") String state);
}
