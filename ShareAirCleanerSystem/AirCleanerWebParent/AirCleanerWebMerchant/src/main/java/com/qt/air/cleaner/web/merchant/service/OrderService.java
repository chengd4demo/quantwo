package com.qt.air.cleaner.web.merchant.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;

@FeignClient(name = "order-service")
public interface OrderService {
	
	/**
	 * 收入明细
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="/inbound/list", method = RequestMethod.POST)
	public ResultInfo queryAccountInbound(@RequestBody RequestParame requestParame) throws BusinessRuntimeException;
	
	
}
