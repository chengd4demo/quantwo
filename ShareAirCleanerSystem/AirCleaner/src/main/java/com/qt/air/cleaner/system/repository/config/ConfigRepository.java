package com.qt.air.cleaner.system.repository.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.system.domain.config.SysConfig;

@Repository
public interface ConfigRepository extends JpaRepository<SysConfig, String>{
	SysConfig findByBussinessId(String bussinessId);
	@Modifying
	@Query(value = "update SysConfig set open=:open where isFlg = true and bussinessId = :bussinessId")
	void updateConfig(@Param("open") Boolean open,@Param("bussinessId") String bussinessId);
}
