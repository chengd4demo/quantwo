package com.qt.air.cleaner.report.batch.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.report.batch.mapper.ScanCodeAmountMapper;
import com.qt.air.cleaner.report.batch.model.ScanCodeAmount;
import com.qt.air.cleaner.report.batch.service.ScanCodeAmountService;

@Service
public class ScanCodeAmountServiceImpl implements ScanCodeAmountService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public ScanCodeAmount findByDeviceName(String deviceName, Date date) {
		String sql = "select t.device_name from rep_scan_code_amount t where t.device_name = ?";
		ScanCodeAmount scanCodeAmount = jdbcTemplate.queryForObject(sql, new ScanCodeAmountMapper(),deviceName);
		return scanCodeAmount;
	}

	@Override
	public List<ScanCodeAmount> findAll() {
		String sql = "select t.* from rep_scan_code_amount t";
		List<ScanCodeAmount> result = jdbcTemplate.query(sql, new ScanCodeAmountMapper());
		return result;
	}
	

}
