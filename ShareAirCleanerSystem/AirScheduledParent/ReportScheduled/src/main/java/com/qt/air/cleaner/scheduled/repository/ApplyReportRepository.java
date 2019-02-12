package com.qt.air.cleaner.scheduled.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.ApplyReport;

@Repository
public interface ApplyReportRepository extends JpaRepository<ApplyReport,String> {
	ApplyReport findByMachNoAndSweepCodeTime(String machNo, Date time);
}
