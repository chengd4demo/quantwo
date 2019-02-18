package com.qt.air.cleaner.market.service.report;

import java.util.List;
import java.util.Map;

import com.qt.air.cleaner.market.vo.report.ApplyReportView;

public interface ApplyReportService {
	/**
	 * 设备使用统计分页查询
	 * 
	 * @param params
	 * @return
	 */
	public List<ApplyReportView> findAllApplyReport(Map<String,Object> params);
}
