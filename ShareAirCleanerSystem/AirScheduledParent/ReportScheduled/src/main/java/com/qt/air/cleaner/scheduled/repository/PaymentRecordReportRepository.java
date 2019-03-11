package com.qt.air.cleaner.scheduled.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.PaymentRecordReport;

@Repository
public interface PaymentRecordReportRepository extends JpaRepository<PaymentRecordReport, String> {
	@Query(value="select t.* from rep_payment_record t where t.mach_no=:machNo and to_char(t.sweep_code_time,'yyyy-mm-dd hh24:mi:ss') like :sweepCodeTime",nativeQuery = true)
	PaymentRecordReport findPaymentRecordReportData(@Param("machNo") String machNo,@Param("sweepCodeTime") String sweepCodeTime);
	PaymentRecordReport findByMachNoAndSweepCodeTime(String machNo, Date time);
	PaymentRecordReport findFirstByOrderBySweepCodeTimeDesc();
	
}
