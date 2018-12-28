package com.qt.air.cleaner.web.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.common.web.service.impl.TokenService;

@RestController
@RequestMapping("/common-channel")
public class CommonController {
	private Logger logger = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private TokenService tokenService;
	@GetMapping("/token")
	public ResultInfo queryDeviceStatus(@RequestParam("openId") String openId) {
		logger.info("execute device-channel's method queryDeviceStatus()  start -> param{}",openId);	
		try {
			return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",tokenService.getToken(openId));
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
}
