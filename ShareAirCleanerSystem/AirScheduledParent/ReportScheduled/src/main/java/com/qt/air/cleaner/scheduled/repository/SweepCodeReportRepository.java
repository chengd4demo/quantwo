package com.qt.air.cleaner.scheduled.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.SweepCodeReport;

@Repository
public interface SweepCodeReportRepository extends JpaRepository<SweepCodeReport, String> {
	SweepCodeReport findByMachNoAndSweepCodeTime(String machNo,Date sweepCodeTime);
}
