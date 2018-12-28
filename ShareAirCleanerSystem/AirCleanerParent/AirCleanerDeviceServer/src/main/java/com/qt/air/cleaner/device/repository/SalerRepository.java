package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.Saler;

@Repository
public interface SalerRepository extends JpaRepository<Saler, String> {
	Saler findByPhoneNumberAndRemoved(String phoneNumber, Boolean removed);
}
