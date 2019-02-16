package com.qt.air.cleaner.market.service.report;

import java.util.List;
import java.util.Map;

import com.qt.air.cleaner.market.vo.report.PaymentRecordView;

public interface PaymentRecordReportService {
	/**
	 * 支付统计查询
	 * 
	 * @param params
	 * @return
	 */
	public List<PaymentRecordView> findAllPaymentRecordReport(Map<String,Object> params);

}
