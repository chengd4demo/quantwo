package com.qt.air.cleaner.web.customer.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;

/**
 * @ClassName: DeviceService
 * @Desc: feign client interfce
 * @Author: Jansan.Ma
 * @Date: 2018年11月21日
 */
@FeignClient(name = "device-server")
public interface DeviceService {
	
	/**
	 * 设备使用价格列表
	 * 
	 * @param machNo
	 * @return
	 */
	@RequestMapping(value = "/query/{deviceId}", method = RequestMethod.GET)
	ResultInfo queryDeviceStatus(@PathVariable("deviceId") String machNo);
	
	/**
	 * 设备监控列表
	 * 
	 * @param requestParame
	 * @return
	 */
	@RequestMapping(value = "/queryDeviceMonitors", method = RequestMethod.POST)
	ResultInfo queryDeviceMonitorPage(@RequestBody RequestParame requestParame);
	
	/**
	 * 设备监控信息
	 * 
	 * @param requestParame
	 * @return
	 */
	@RequestMapping(value = "/queryDeviceMonitor/{deviceId}", method = RequestMethod.POST)
	ResultInfo queryDeviceMonitor(@PathVariable("deviceId") String machNo);
	
	/**
	 * 设备控制(关)
	 * @param machNo
	 * @return
	 */
	@RequestMapping(value = "/deviceTurnOff/{deviceId}", method = RequestMethod.POST)
	ResultInfo deviceTurnOff(@PathVariable("deviceId") String machNo);
	
	/**
	 * 设备控制(开)
	 * @param parame
	 * @return
	 */
	@RequestMapping(value = "/deviceTurnNo", method = RequestMethod.POST)
	ResultInfo deviceTurnOn(@RequestBody Map<String,Object> parame);
	
	/**
	 * 投资商下商家信息
	 * @param parame
	 * @return
	 */
	@RequestMapping(value = "/investorForTrader",method = RequestMethod.POST)
	ResultInfo queryInvestorForTrader(@RequestBody RequestParame requestParame);
	
	/** 设备详细
	 * @param parame
	 * @return
	 */
	@RequestMapping(value = "/findByDeviceSequence",method = RequestMethod.GET)
	String queryDevice(@RequestParam("deviceSequence") String deviceSequence);
}
