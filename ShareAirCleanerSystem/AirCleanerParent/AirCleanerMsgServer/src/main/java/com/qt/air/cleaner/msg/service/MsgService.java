package com.qt.air.cleaner.msg.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qt.air.cleaner.base.dto.ResultInfo;

@RequestMapping
public interface MsgService {

	/**
	 * 短信验证码发送
	 * 
	 * @param phoneNumber
	 * @return
	 */
	@RequestMapping("sendSms/{phoneNumber}")
	ResultInfo sendSms(@PathVariable("phoneNumber") String phoneNumber);
}
