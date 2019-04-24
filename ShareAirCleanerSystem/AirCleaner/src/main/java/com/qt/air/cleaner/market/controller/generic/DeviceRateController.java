package com.qt.air.cleaner.market.controller.generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.service.generic.DeviceRateService;
import com.qt.air.cleaner.market.vo.generic.DeviceRateView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/device/rate")
public class DeviceRateController {

	private final Logger logger = LoggerFactory.getLogger(DeviceRateController.class);
	private final String GENERIC_DEVICE_RATE = "view/market/generic/queryDeviceRateIndex";
	
	@Autowired
	DeviceRateService deviceRateService;
	
	@RequestMapping (method = RequestMethod.GET,path = "/index")
	public String index() {
		return GENERIC_DEVICE_RATE;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<DeviceRateView> page(Integer page,Integer limit,DeviceRateView params){
		logger.debug("频率使用率监控列表请求参数对象：{}",params.toString());
		Page<DeviceRateView> deviceRates= deviceRateService.findAllDeviceRate(params, 
				new PageRequest(page-1, limit));
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,deviceRates.getContent(),deviceRates.getTotalElements());
	}
}
