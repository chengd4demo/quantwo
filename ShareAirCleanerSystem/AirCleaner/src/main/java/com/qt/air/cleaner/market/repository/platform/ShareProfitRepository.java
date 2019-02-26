package com.qt.air.cleaner.market.repository.platform;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.platform.ShareProfit;

@Repository
public interface ShareProfitRepository extends JpaRepository<ShareProfit, String> {
	ShareProfit findByType(String type);
	@Modifying
	@Query(value="update ShareProfit s set s.free=:free")
	void updateAllFree(@Param("free") Float free);
	Page<ShareProfit> findAll(Specification<ShareProfit> specification, Pageable pageable);
	ShareProfit findById(String id);
	@Query(value="select t.id,t.free,t.scale,t.pid,t.name,t.type,t.agent_id from ps_share_profit t start with id = :distributionRatio "+
			"connect by prior t.id = t.pid",nativeQuery = true)
	List<ShareProfit> findByPlatformSet(@Param("distributionRatio") String distribution);
}
