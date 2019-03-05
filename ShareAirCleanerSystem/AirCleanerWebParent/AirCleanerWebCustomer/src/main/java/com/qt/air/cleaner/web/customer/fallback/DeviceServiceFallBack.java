package com.qt.air.cleaner.web.customer.fallback;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.enums.ErrorCodeEnum;
import com.qt.air.cleaner.web.customer.service.DeviceService;

@Component
public class DeviceServiceFallBack implements DeviceService {
	Logger logger = LoggerFactory.getLogger(DeviceServiceFallBack.class);
	@Override
	public ResultInfo queryDeviceStatus(String machNo) {
		logger.warn("获取价格信息失败，设备序列号：{}",machNo);
		Map<String,Object> data = new HashMap<>();
			data.put("deviceId", "");
			data.put("onLine", 0);
			data.put("turnOn", 0);
			data.put("pm25", 0);
			data.put("available", false);
			data.put("message", "空气净化器离线!");
			data.put("price", new String[0]);
		return new ResultInfo(ErrorCodeEnum.ES_1012.getErrorCode(),ErrorCodeEnum.ES_1012.getMessage(),data);
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
	public ResultInfo deviceTurnOn(Map<String, Object> parame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo deviceTurnOff(String machNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryInvestorForTrader(RequestParame requestParame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryDevice(String deviceSequence) {
		// TODO Auto-generated method stub
		return null;
	}


}
