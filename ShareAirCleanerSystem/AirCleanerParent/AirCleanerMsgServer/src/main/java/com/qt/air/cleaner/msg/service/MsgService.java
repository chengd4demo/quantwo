package com.qt.air.cleaner.msg.service;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qt.air.cleaner.base.dto.ResultInfo;

@RequestMapping
public interface MsgService {

	/**
	 * 短信验证码发送
	 * 
	 * @param phoneNumber
	 * @param templateCode
	 * @return
	 */
	@RequestMapping(value="sendSms/{phoneNumber}", method = RequestMethod.GET)
	ResultInfo sendSms(@PathVariable("phoneNumber") String phoneNumber,@RequestParam("templateCode") String templateCode);
	
	
	/**
	 * 短信验证码校验
	 * 
	 * @param parames
	 * @return
	 */
	@RequestMapping(value="checked/validVerificationCode", method = RequestMethod.POST)
	ResultInfo checkedValidVerificationCode(@RequestBody Map<String, String> parames);
}
