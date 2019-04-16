package com.qt.air.cleaner.market.repository.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.generic.Device;

@Repository
public interface DeviceRateRepository extends JpaRepository<Device, String>{
	
	Page<Device> findAll(Specification<Device> specification, Pageable pageable);
}
