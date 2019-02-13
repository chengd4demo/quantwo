package com.qt.air.cleaner.market.service.report;

import java.util.List;
import java.util.Map;

import com.qt.air.cleaner.market.vo.report.ApplyReportView;

public interface ApplyReportService {
	/**
	 * 扫码统计分页查询
	 * 
	 * @param params
	 * @return
	 */
	public List<ApplyReportView> findAllApplyReport(Map<String,String> params);
}
