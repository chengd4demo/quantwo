package com.qt.air.cleaner.market.controller.generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qt.air.cleaner.market.service.generic.DeviceBatchService;
import com.qt.air.cleaner.market.service.generic.InvestorService;

@Controller
@RequestMapping("market/device/monitor")
public class DeviceMonitorController {
	private final Logger logger = LoggerFactory.getLogger(DeviceMonitorController.class);
	private final String GENERIC_DEVICE_MONITOR_INDEX = "view/market/generic/queryDeviceMonitorIndex",
			GENERIC_DEVICE_MONITOR_TREE_INDEX = "view/market/generic/monitorTree";
	@Autowired
	DeviceBatchService deviceBatchService;
	@Autowired
	InvestorService investorService;

	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_DEVICE_MONITOR_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/tree/index")
	public String tree() {
		return GENERIC_DEVICE_MONITOR_TREE_INDEX;
	}

	
}
