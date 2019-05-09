package com.qt.air.cleaner.market.repository.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.generic.DeviceChipIn;

@Repository
public interface DeviceChipRepository extends JpaRepository<DeviceChipIn, String>{

	DeviceChipIn findByMachNo(String machNo);

}
