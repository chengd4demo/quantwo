package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.ApplyReport;

@Repository
public interface ApplyReportRepository extends JpaRepository<ApplyReport,String> {
	@Query(value="select * from ( select t.id, t.create_time, t.creater, t.operate_time, t.operator, t.removed, t.company_id, t.investor_id, t.mach_no, t.saler_id, t.sweep_code_time, t.total, t.trader_id from rep_apply_record t order by t.sweep_code_time desc ) " + 
	"where mach_no = :machNo  and to_char(sweep_code_time,'yyyy-mm-dd hh24:mi:ss') like :sweepCodeTime and rownum <= 1",nativeQuery = true)
	ApplyReport findSweepCodeReportData(@Param("machNo") String machNo,@Param("sweepCodeTime") String sweepCodeTime);
}
