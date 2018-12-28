package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.PriceModel;

@Repository
public interface PriceModelRepository extends JpaRepository<PriceModel, String>{
	PriceModel findByIdAndRemoved(String id,Boolean removed);
}
