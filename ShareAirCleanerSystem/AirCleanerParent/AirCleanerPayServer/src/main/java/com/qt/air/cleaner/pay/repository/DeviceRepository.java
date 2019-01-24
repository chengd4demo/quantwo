package com.qt.air.cleaner.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.pay.domain.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
	Device findByDeviceSequence(String deviceSequence);
}
