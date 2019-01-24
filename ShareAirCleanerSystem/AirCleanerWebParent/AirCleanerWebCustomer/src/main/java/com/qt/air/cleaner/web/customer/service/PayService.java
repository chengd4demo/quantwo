package com.qt.air.cleaner.web.customer.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;

@FeignClient(name = "pay-server")
public interface PayService {

	/**
	 * 支付授权
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="/pay/auth", method = RequestMethod.POST)
	public ResultInfo payAuth(@RequestBody RequestParame requestParame) throws BusinessRuntimeException;
	
	/**
	 * 创建预支付订单
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="create/billing", method = RequestMethod.POST)
	public ResultInfo createBilling(@RequestBody RequestParame requestParame)  throws BusinessRuntimeException;
	
	/**
	 * 支付请求
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="complete/billing", method = RequestMethod.POST)
	public ResultInfo completeBilling(@RequestBody RequestParame requestParame)  throws BusinessRuntimeException;
	
	/**
	 * 微信回调通知
	 * 
	 * @param strXML
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="billing/notify", method = RequestMethod.POST)
	public ResultInfo billingNotify(@RequestParam("strXML") String strXML) throws BusinessRuntimeException;
	
	/**
	 * 支付信息处理
	 * 
	 * @param type
	 * @param billingNumber
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="wx/msg", method = RequestMethod.POST)
	public ResultInfo weiXinMsg(@RequestParam("type") String type,@RequestParam("billingNumber") String billingNumber) throws BusinessRuntimeException;
	
}
