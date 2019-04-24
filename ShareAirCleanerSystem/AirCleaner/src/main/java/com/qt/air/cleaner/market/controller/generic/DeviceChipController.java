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
import com.qt.air.cleaner.market.service.generic.DeviceChipService;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/device/chip")
public class DeviceChipController {

	private final Logger logger = LoggerFactory.getLogger(DeviceChipController.class);
	private final String GENERIC_DEVICE_CHIP = "view/market/generic/queryDeviceChipIndex";
	
	@Autowired
	DeviceChipService deviceChipService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_DEVICE_CHIP;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<DeviceChip> page(Integer page,Integer limit,DeviceChipView params,String deviceBatchId){
		logger.debug("设备滤芯监控列表请求参数对象：{}",params.toString());
		Page<DeviceChip> deviceChips= deviceChipService.findAllDevice(params, 
				new PageRequest(page-1, limit));
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,deviceChips.getContent(),deviceChips.getTotalElements());
	}
}
