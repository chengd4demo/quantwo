package com.qt.air.cleaner.scheduled.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qt.air.cleaner.scheduled.service.ApplyReportService;
import com.qt.air.cleaner.scheduled.service.PaymentRecordReportService;
import com.qt.air.cleaner.scheduled.service.ReportBillingService;
import com.qt.air.cleaner.scheduled.service.SweepCodeReportService;

@Service
public class ReportBillingServiceImpl implements ReportBillingService {
	@Resource
	ApplyReportService applyReportService;
	@Resource
	PaymentRecordReportService paymentRecordReportService;
	@Resource
	SweepCodeReportService sweepCodeReportService;
	@Override
	public void startReportBilling(Date currentTime) {
		applyReportService.jobApplyReport(currentTime);
		paymentRecordReportService.jobPaymentRecordReport(currentTime);
		sweepCodeReportService.jobSweepCodeReport(currentTime);
	}

}
