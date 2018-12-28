package com.qt.air.cleaner.market.repository.platform;

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
}
