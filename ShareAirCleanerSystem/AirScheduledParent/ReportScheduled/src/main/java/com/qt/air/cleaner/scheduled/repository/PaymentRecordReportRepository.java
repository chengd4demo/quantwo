package com.qt.air.cleaner.scheduled.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.PaymentRecordReport;

@Repository
public interface PaymentRecordReportRepository extends JpaRepository<PaymentRecordReport, String> {

	PaymentRecordReport findByMachNoAndSweepCodeTime(String machNo, Date time);
	
}
