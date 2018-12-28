package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.Trader;

@Repository
public interface TraderRepository extends JpaRepository<Trader,String>{
	Trader findByPhoneNumberAndRemoved(String phoneNumber, Boolean removed);
}
