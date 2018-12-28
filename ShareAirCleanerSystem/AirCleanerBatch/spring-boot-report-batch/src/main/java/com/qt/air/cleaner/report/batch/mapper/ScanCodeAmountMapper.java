package com.qt.air.cleaner.report.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.qt.air.cleaner.report.batch.model.ScanCodeAmount;

public class ScanCodeAmountMapper implements RowMapper<ScanCodeAmount> {

	@Override
	public ScanCodeAmount mapRow(ResultSet resultSet, int i) throws SQLException {
		ScanCodeAmount scanCodeAmount = new ScanCodeAmount();
		scanCodeAmount.setId(resultSet.getString("id"));
		scanCodeAmount.setBillingTime(resultSet.getDate("billing_time"));
		scanCodeAmount.setCount(resultSet.getInt("count"));
		scanCodeAmount.setCreateTime(resultSet.getDate("create_time"));
		scanCodeAmount.setDeviceName(resultSet.getString("device_name"));
		scanCodeAmount.setInvestorName(resultSet.getString("investor_name"));
		scanCodeAmount.setTraderName(resultSet.getString("trader_name"));
		return null;
	}

}
