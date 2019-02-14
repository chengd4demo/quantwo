package com.qt.air.cleaner.market.service.report;

import java.util.List;
import java.util.Map;

import com.qt.air.cleaner.market.vo.report.SweepCodeReportView;

public interface SweepCodeReportService {
	/**
	 * 扫码统计分页查询
	 * 
	 * @param params
	 * @return
	 */
	public List<SweepCodeReportView> findAllApplyReport(Map<String,Object> params);
}
