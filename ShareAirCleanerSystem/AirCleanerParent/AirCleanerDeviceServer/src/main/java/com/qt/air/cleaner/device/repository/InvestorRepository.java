package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.Investor;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {
	Investor findByPhoneNumberAndRemoved(String phoneNumber, Boolean removed);
}
