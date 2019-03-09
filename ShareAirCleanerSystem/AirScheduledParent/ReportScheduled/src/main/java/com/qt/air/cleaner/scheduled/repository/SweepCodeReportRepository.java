package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.SweepCodeReport;

@Repository
public interface SweepCodeReportRepository extends JpaRepository<SweepCodeReport, String> {
	@Query(value="select t.* from rep_sweep_code t where t.mach_no=:machNo and to_char(t.sweep_code_time,'yyyy-mm-dd hh24:mi:ss') like :sweepCodeTime",nativeQuery = true)
	SweepCodeReport findSweepCodeReportData(@Param("machNo") String machNo,@Param("sweepCodeTime") String sweepCodeTime);
	SweepCodeReport findFirstByOrderBySweepCodeTimeDesc();
}
