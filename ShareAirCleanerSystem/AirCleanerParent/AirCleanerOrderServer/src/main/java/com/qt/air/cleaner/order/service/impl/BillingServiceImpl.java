package com.qt.air.cleaner.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.order.domain.Billing;
import com.qt.air.cleaner.order.domain.Device;
import com.qt.air.cleaner.order.domain.PriceValue;
import com.qt.air.cleaner.order.repository.BillingRepository;
import com.qt.air.cleaner.order.service.BillingService;
import com.qt.air.cleaner.order.vo.BillingView;


@RestController
@Transactional
public class BillingServiceImpl implements BillingService {
	private Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);
	@Resource
	BillingRepository billingRepository;
	
	/**
	 * 创建订单
	 * 
	 * @param billing
	 * @return
	 */
	@Override
	public ResultInfo createBilling(@RequestBody BillingView billing) throws BusinessRuntimeException {
		if(null != billing) {
			logger.info("execute method createBilling() param --> billing:{}", billing.toString());
			Billing inBilling = new Billing();
			Device device = billing.getDevice();
			PriceValue priceValue = billing.getPriceValue();
			inBilling.setDeviceId(device.getId());
			inBilling.setBillingId(generateBillingNumber(device));
			inBilling.setMachNo(device.getMachNo());
			inBilling.setCostTime(priceValue.getCostTime());
			inBilling.setPriceId(priceValue.getId());
			inBilling.setAmount(priceValue.getRealValue());
			inBilling.setUnitPrice(priceValue.getValue());
			inBilling.setDiscount(priceValue.getDiscount());
			inBilling.setCreater(billing.getOpenId());
			billingRepository.save(inBilling);
			return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", inBilling.getBillingId());
		} else {
			throw new BusinessRuntimeException(ResultCode.R2001.code,ResultCode.R2001.info);
		}
	}
	
	/**
	 * 生成订单号
	 * @param device
	 * 
	 * @return
	 */
	private String generateBillingNumber(Device device){
		String billingNumber = device.getMachNo();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		return billingNumber + format.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 更新订单
	 * 
	 * @param billingNumber 订单号
	 * @param transactionId 流水号
	 * @param state 订单状态
	 * @param errorCode 错误码
	 * @param errorMsg 错误信息
	 * 
	 * @return
	 */
	@Override
	public ResultInfo updateBilling(String billingNumber, String transactionId, Integer state, String errorCode,
			String errorMsg) throws BusinessRuntimeException {
		logger.info("execute method updateBilling() param --> billingNumber:{}", billingNumber);
		Billing billing = billingRepository.findByBillingId(billingNumber);
		billing.setState(state);
		if (StringUtils.isNotBlank(transactionId)) {
			billing.setTransactionId(transactionId);
		}
		if (StringUtils.isNotBlank(errorCode)) {
			billing.setErrorCode(errorCode);
		}
		if (StringUtils.isNotBlank(errorMsg)) {
			billing.setErrorMsg(errorMsg);
		}
		billingRepository.saveAndFlush(billing);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
	}
	
	/**
	 * 更新预支付交易会话标识
	 * 
	 * @param billingNumber 订单号
	 * @param weixin 预支付交易会话标识
	 * 
	 * @return
	 */
	@Override
	public ResultInfo updateWeiXin(String billingNumber, String weixin) throws BusinessRuntimeException {
		logger.info("execute method updateWeiXin() param --> billingNumber:{}", billingNumber);
		Billing billing = billingRepository.findByBillingId(billingNumber);
		billing.setWeixin(weixin);
		billing.setState(Billing.BILLING_STATE_PENDING_PAYMENT);
		billingRepository.saveAndFlush(billing);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
	}
	
	/**
	 * 查询订单
	 * 
	 * @param billingNumber 订单号
	 * 
	 * @return
	 */
	@Override
	public ResultInfo queryBilling(String billingNumber) throws BusinessRuntimeException {
		logger.info("execute method queryBilling() param --> billingNumber:{}", billingNumber);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", billingRepository.findByBillingId(billingNumber));
	}

	@Override
	public ResultInfo redis(String parame) {
		stringRedisTemplate.opsForValue().set("token:aaa", parame);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
	}
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
}
