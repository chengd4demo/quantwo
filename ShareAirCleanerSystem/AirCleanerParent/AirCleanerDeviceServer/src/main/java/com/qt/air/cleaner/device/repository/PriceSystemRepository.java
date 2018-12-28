package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.PriceSystem;

@Repository
public interface PriceSystemRepository extends JpaRepository<PriceSystem, String>{
	PriceSystem findByIdAndRemoved(String id,Boolean removed);
}
