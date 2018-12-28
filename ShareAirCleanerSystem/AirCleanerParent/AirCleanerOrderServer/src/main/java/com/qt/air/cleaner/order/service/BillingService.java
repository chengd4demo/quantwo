package com.qt.air.cleaner.order.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.order.vo.BillingView;


@RequestMapping
public interface BillingService {
	/**
	 * 创建订单
	 * 
	 * @param billing
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ResultInfo createBilling(BillingView billing) throws BusinessRuntimeException;
	
	
	/**
	 * 更新订单信息
	 * 
	 * @param billingNumber
	 * @param transactionId
	 * @param state
	 * @param errorCode
	 * @param errorMsg
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public ResultInfo updateBilling(String billingNumber, String transactionId, Integer state, String errorCode,
	        String errorMsg) throws BusinessRuntimeException;
	
	/**
	 * 更新预支付交易会话标识
	 * 
	 * @param billingNumber
	 * @param weixin
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="/updateWeiXin", method = RequestMethod.POST)
	public ResultInfo updateWeiXin(String billingNumber, String weixin) throws BusinessRuntimeException;
	
	/**
	 * 查询订单
	 * 
	 * @param billingNumber
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value="/query", method = RequestMethod.POST)
	public ResultInfo queryBilling(String billingNumber)  throws BusinessRuntimeException;
	
	/**
	 * redis测试
	 * 
	 * @param parame
	 * @return
	 */
	@RequestMapping("/redis")
	public ResultInfo redis(String parame);
}
