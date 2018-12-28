package com.qt.air.cleaner.web.customer.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
	 * @return
	 */
	@RequestMapping("sendSms/{phoneNumber}")
	ResultInfo sendSms(@PathVariable("phoneNumber") String phoneNumber);
}
