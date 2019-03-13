package com.qt.air.cleaner.web.merchant.fallback;

import java.util.Map;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.merchant.service.DeviceService;

public class DeviceServiceFallBack implements DeviceService {

	@Override
	public ResultInfo queryDeviceStatus(String machNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryDeviceMonitorPage(RequestParame requestParame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryDeviceMonitor(String machNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo deviceTurnOff(String machNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo deviceTurnOn(Map<String, Object> parame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryInvestorForTrader(RequestParame requestParame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryDeviceCounts(RequestParame requestParame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryTurnState(String machNo) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
