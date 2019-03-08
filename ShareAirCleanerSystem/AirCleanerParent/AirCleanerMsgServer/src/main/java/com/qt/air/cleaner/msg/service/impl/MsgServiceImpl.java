package com.qt.air.cleaner.msg.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.utils.SmsUtils;
import com.qt.air.cleaner.msg.service.MsgService;

import net.sf.json.util.JSONUtils;

@RestController
public class MsgServiceImpl implements MsgService {
	private static Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public ResultInfo sendSms(@PathVariable("phoneNumber") String phoneNumber,@RequestParam("templateCode") String templateCode) {
		String smsCode = null;
		logger.info("execute method sendSms() param --> phoneNumber: " + phoneNumber + ",templateCode: " + templateCode);
		if(StringUtils.isNotBlank(phoneNumber)) {
			smsCode = generateNumber(5);
			try {
				SendSmsResponse sendSmsResponse = SmsUtils.sendSms(phoneNumber, smsCode,templateCode);
				logger.debug("短信接口返回的数据----------------");
				logger.debug("Code=" + sendSmsResponse.getCode());
		        logger.debug("Message=" + sendSmsResponse.getMessage());
		        logger.debug("RequestId=" + sendSmsResponse.getRequestId());
		        logger.debug("BizId=" + sendSmsResponse.getBizId());
				logger.debug(JSONUtils.valueToString(sendSmsResponse));
				if(StringUtils.equals("OK",sendSmsResponse.getCode())) {
					stringRedisTemplate.opsForValue().set(phoneNumber, smsCode,5,TimeUnit.MINUTES);
				} else {
					smsCode = null;
					logger.error("execute outside sms api error  --> {}", sendSmsResponse.getMessage());
				}
			} catch (ClientException e) {
				logger.error("execute method sendSms error --> {}", e.getErrMsg());
				e.printStackTrace();
				return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success",null);
			}
		} else {
			logger.warn("execute method sendSms parameter error --> {}", ResultCode.R2001.info);
			return new ResultInfo(ResultCode.R2001.code, ResultCode.R2001.info, smsCode);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", smsCode);
	}
	
	private String generateNumber(int len) {
		return RandomStringUtils.randomNumeric(len);
	}	
}
