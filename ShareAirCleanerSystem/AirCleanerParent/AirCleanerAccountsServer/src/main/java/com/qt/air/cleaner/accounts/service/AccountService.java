package com.qt.air.cleaner.accounts.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;

@RequestMapping
public interface AccountService {
	
	/**
	 * 账务查询
	 * 
	 * @param weixin
	 * @return
	 */
	@RequestMapping(value = "/query/{weixin}", method = RequestMethod.POST)
	ResultInfo queryAccountDetailByWeixin(@PathVariable("weixin") String weixin);
	
	
	/**
	 * 提现记录
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value = "/queryAccountOutbounds/", method = RequestMethod.POST)
	ResultInfo queryAccountOutboundPage(@RequestBody RequestParame requestParame) throws BusinessRuntimeException;

	
	/**
	 * 取消提现
	 * 
	 * @param id
	 * @return
	 * @throws BusinessRuntimeException 
	 */
	@RequestMapping(value = "/cleanAccountOutbound/{id}", method = RequestMethod.POST)
	ResultInfo cleanAccountOutbound(@PathVariable String id);
	
	
}
