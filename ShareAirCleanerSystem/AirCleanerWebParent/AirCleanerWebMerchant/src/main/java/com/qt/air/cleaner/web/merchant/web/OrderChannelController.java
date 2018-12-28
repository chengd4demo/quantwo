package com.qt.air.cleaner.web.merchant.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.merchant.service.OrderService;
import com.qt.air.cleaner.base.dto.RequestParame;

@RestController
@RequestMapping("/order-channel")
public class OrderChannelController {
	private static Logger logger = LoggerFactory.getLogger(OrderChannelController.class);
	@Autowired
	private OrderService orderService;
	@PostMapping("/queryInbound")
	public ResultInfo queryInbound(@RequestBody RequestParame requestParame) {
		logger.info("execute order-channel's method queryInbound()  start -> param{}",requestParame);
		try {
			return orderService.queryAccountInbound(requestParame);
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
}
