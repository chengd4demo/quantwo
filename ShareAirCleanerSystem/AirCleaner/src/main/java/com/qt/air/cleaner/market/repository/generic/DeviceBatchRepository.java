package com.qt.air.cleaner.market.repository.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;

@Repository
public interface DeviceBatchRepository extends JpaRepository<DeviceBatch, String> {
	Page<DeviceBatch> findAll(Specification<DeviceBatch> spec, Pageable pageable);
	DeviceBatch findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update DeviceBatch I set I.removed = true where I.id = :id")
	void deleteDeviceBatch(@Param("id") String id);
}
