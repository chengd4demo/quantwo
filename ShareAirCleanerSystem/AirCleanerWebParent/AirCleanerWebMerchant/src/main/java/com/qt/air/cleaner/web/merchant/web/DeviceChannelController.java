package com.qt.air.cleaner.web.merchant.web;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.merchant.service.DeviceService;
import com.qt.air.cleaner.web.merchant.vo.DeviceMonitor;

/**
 * @ClassName: DeviceChannelController
 * @Desc: 设备客户端Api
 * @Author: Jansan.Ma
 * @Date: 2018年11月20日
 */
@RestController
@RequestMapping("/device-channel")
public class DeviceChannelController {
	private static Logger logger = LoggerFactory.getLogger(DeviceChannelController.class);
	/**注入设备服务接口**/
	@Autowired
	private DeviceService deviceService;
	@GetMapping("/query/{deviceId}")
	public ResultInfo queryDeviceStatus(@PathVariable("deviceId") String machNo) {
		logger.info("execute device-channel's method queryDeviceStatus()  start -> param{}",machNo);	
		try {
			return deviceService.queryDeviceStatus(machNo);
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	@PostMapping("/queryDeviceMonitor/{deviceId}")
	public ResultInfo queryDeviceMonitor(@PathVariable("deviceId") String machNo) {
		logger.info("execute device-channel's method queryDeviceMonitor()  machNo -> {}",machNo);
		DeviceMonitor deviceMonitor =  null;
		
		try {
			ResultInfo resultInfo = deviceService.queryDeviceMonitor(machNo);
			if(resultInfo != null && StringUtils.equals(String.valueOf(ResultCode.SC_OK), resultInfo.getStatus())) {
				Gson gson = new Gson();
				if(resultInfo.getData() != null) {
					@SuppressWarnings("unchecked")
					Map<String,Object> result  = gson.fromJson(gson.toJson(resultInfo.getData()), Map.class);
					deviceMonitor = new DeviceMonitor();
					deviceMonitor.setMachNo(result.get("machno").toString());
					deviceMonitor.setDeviceState(result.get("devicestate").toString());
					deviceMonitor.setCostTime(result.get("costtime") + "小时");
					deviceMonitor.setLastTime(result.get("lasttime") + "小时");
				}
				
			}
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", deviceMonitor);
	}
	
	@PostMapping("/deviceTurnOn")
	public ResultInfo deviceTurnNo(@RequestBody Map<String,Object> parame) {
		logger.info("execute method DeviceTurnOn() param --> parame:{}", parame);
		try {
			return deviceService.deviceTurnOn(parame);
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), parame);
		}
	}
	
	@PostMapping("/deviceTurnOff/{deviceId}")
	public ResultInfo deviceTurnOff(@PathVariable("deviceId") String machNo) {
		logger.info("execute device-channel's method deviceTurnOff()  machNo -> {}",machNo);
		try {
			return deviceService.deviceTurnOff(machNo);
		} catch (Exception e) {
			logger.error("system error:{}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(),machNo);
		}
	}
	
	@PostMapping("/investorForTrader")
	ResultInfo queryInvestorForTrader(@RequestBody RequestParame requestParame) {
		logger.info("execute method DeviceTurnOn() param --> requestParame:{}", requestParame);
		try {
			return deviceService.queryInvestorForTrader(requestParame);
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), requestParame);
		}
	}
	
	@PostMapping("/queryDeviceMonitors")
	ResultInfo queryDeviceMonitorPage(@RequestBody RequestParame requestParame) {
		logger.info("execute method queryDeviceMonitorPage() param --> requestParame:{}", requestParame);
		try {
			return deviceService.queryDeviceMonitorPage(requestParame);
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), requestParame);
		}
	}
	
	@PostMapping("/queryDeviceCounts")
	ResultInfo queryDeviceCounts(@RequestBody RequestParame requestParame) {
		logger.info("execute method queryDeviceCounts() param --> requestParame:{}",requestParame);
		try {
			return deviceService.queryDeviceCounts(requestParame);
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), requestParame);
		}
	}
	
	@PostMapping("/queryTurnState/{machNo}")
	ResultInfo queryTurnState(@PathVariable("machNo") String machNo) {
		logger.info("execute method queryTurnState() param --> requestParame:{}",machNo);
		try {
			return deviceService.queryTurnState(machNo);
		} catch (Exception e) {
			logger.error("system error:{}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), machNo);
		}
		}
	}
