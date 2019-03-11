package com.qt.air.cleaner.scheduled.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.ApplyReport;

@Repository
public interface ApplyReportRepository extends JpaRepository<ApplyReport,String> {
	ApplyReport findByMachNoAndSweepCodeTime(String machNo, Date time);
	ApplyReport findFirstByOrderBySweepCodeTimeDesc();
	@Query(value="select t.* from rep_apply_record t where t.mach_no=:machNo and to_char(t.sweep_code_time,'yyyy-mm-dd hh24:mi:ss') like :sweepCodeTime",nativeQuery = true)
	ApplyReport findSweepCodeReportData(@Param("machNo") String machNo,@Param("sweepCodeTime") String sweepCodeTime);
}
