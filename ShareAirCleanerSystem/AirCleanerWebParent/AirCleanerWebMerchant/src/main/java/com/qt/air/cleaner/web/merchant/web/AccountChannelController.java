package com.qt.air.cleaner.web.merchant.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.merchant.service.AccountService;
import com.qt.air.cleaner.web.merchant.vo.AccountOutBound;

@RestController
@RequestMapping("/account-channel")
public class AccountChannelController {
	private static Logger logger = LoggerFactory.getLogger(AccountChannelController.class);
	/** 注入设备服务接口 **/
	@Autowired
	private AccountService accountService;

	/** 账务查询 **/
	@PostMapping("/query/{weixin}")
	public ResultInfo queryAccount(@PathVariable("weixin") String weixin) {
		logger.info("execute device-channel's method queryDeviceStatus()  start -> param{}", weixin);
		try {
			return accountService.queryAccountDetailByWeixin(weixin);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}

	/** 提现记录 **/
	@PostMapping("/queryAccountOutBounds")
	public ResultInfo queryAccountOutBound(@RequestBody RequestParame requestParame) {
		logger.info("execute method queryAccountOutBounds() param --> requestParame:{}",
				new Gson().toJson(requestParame));
		try {
			return accountService.queryAccountOutboundPage(requestParame);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), requestParame);
		}
	}
	
	/** 取消提现**/
	@PostMapping("/cleanAccountOutbound/{id}")
	public ResultInfo cleanAccountOutbound(@PathVariable("id") String id) {
		logger.info("execute method cleanAccountOutbound() param --> id:{}",id);
		try {
			return accountService.cleanAccountOutbound(id);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), id);
		}
		
	}
	
	/**申请提现 **/
	@PostMapping(value = "/applyForAccountOutbound")
	public ResultInfo applyForAccountOutbound(@RequestBody AccountOutBound accountOutBound) {
		logger.info("execute method applyForAccountOutbound() start --> parame:{}", accountOutBound.toString());
		try {
			Map<String,String> parames = new HashMap<>();
			parames.put("password", accountOutBound.getPassword());
			parames.put("amount", accountOutBound.getAmount().toString());
			parames.put("weixin", accountOutBound.getWeixin());
			parames.put("userType", accountOutBound.getUserType());
			parames.put("identificationNumber", accountOutBound.getIdentificationNumber());
			parames.put("name", accountOutBound.getName());
			return accountService.applyForAccountOutbound(parames);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), accountOutBound);
		}
		
	}
}
