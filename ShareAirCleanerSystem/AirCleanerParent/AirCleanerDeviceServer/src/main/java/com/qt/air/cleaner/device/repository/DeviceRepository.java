package com.qt.air.cleaner.device.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
	Device findByMachNo(String machNo);
	Page<Device> findAll(Specification<Device> specification, Pageable pageable);
	Device findByDeviceSequence(String deviceSequence);
	List<Device> findByInvestor_Id(String id);
}
