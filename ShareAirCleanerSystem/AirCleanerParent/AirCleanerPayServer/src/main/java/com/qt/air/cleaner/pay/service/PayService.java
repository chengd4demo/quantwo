package com.qt.air.cleaner.pay.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.pay.domain.Device;
import com.qt.air.cleaner.pay.domain.PriceValue;

@RequestMapping
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
	 * 生成或获取JSAPI凭证
	 * 
	 * @param code
	 * @param priceId
	 * @param ipAddress
	 * @param device
	 * @param priceValue
	 * @return
	 */
	@RequestMapping(value="create/jsapi", method = RequestMethod.POST)
	public ResultInfo createJsapi(String code,String priceId,String ipAddress,@RequestBody Device device,@RequestBody PriceValue priceValue);
}
