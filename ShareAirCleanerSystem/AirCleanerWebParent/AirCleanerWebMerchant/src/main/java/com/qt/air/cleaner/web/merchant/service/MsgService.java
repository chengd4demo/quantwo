package com.qt.air.cleaner.web.merchant.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qt.air.cleaner.base.dto.ResultInfo;

/**
 * @ClassName: DeviceService
 * @Desc: feign client interfce
 * @Author: Jansan.Ma
 * @Date: 2018年12月17日
 */
@FeignClient(name = "msg-service")
public interface MsgService {

	/**
	 * 短信验证码发送
	 * 
	 * @param phoneNumber
	 * @param templateCode 
	 * @return
	 */
	@RequestMapping(value="sendSms/{phoneNumber}", method = RequestMethod.GET)
	ResultInfo sendSms(@PathVariable("phoneNumber") String phoneNumber, @RequestParam("templateCode") String templateCode);
	
	/**
	 * 短信验证码校验
	 * 
	 * @param parames
	 * @return
	 */
	@RequestMapping(value="checked/validVerificationCode", method = RequestMethod.POST)
	ResultInfo checkedValidVerificationCode(@RequestBody Map<String, String> parames);
}
