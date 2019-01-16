package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

}
