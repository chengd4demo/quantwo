package com.qt.air.cleaner.scheduled.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.ShareProfit;

@Repository
public interface ShareProfitRepository extends JpaRepository<ShareProfit, String> {
	ShareProfit findById(String id);
	@Query(value="select t.id,t.free,t.scale,t.pid,t.name,t.type,t.agent_id from ps_share_profit t start with id = :distributionRatio "+
			"connect by prior t.id = t.pid",nativeQuery = true)
	List<ShareProfit> findByPlatformSet(@Param("distributionRatio") String distribution);
	
}
