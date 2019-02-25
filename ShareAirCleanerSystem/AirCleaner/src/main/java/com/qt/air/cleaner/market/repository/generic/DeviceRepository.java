package com.qt.air.cleaner.market.repository.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.qt.air.cleaner.market.domain.generic.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>{
	Page<Device> findAll(Specification<Device> specification, Pageable pageable);
	Device findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update Device I set I.removed = true where I.id = :id")
	void deleteDevice(@Param("id") String id);
	Device findByDistributionRatioAndRemoved(String distributionRatio,Boolean removed);
	
}
