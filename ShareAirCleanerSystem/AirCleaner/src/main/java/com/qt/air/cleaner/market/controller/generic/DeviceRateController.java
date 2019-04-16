package com.qt.air.cleaner.market.controller.generic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.service.generic.CompanyService;
import com.qt.air.cleaner.market.service.generic.DeviceRateService;
import com.qt.air.cleaner.market.service.generic.DeviceService;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.service.generic.SalerService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.vo.generic.DeviceRateView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/device/rate")
public class DeviceRateController {

	private final Logger logger = LoggerFactory.getLogger(DeviceRateController.class);
	private final String GENERIC_DEVICE_RATE = "view/market/generic/queryDeviceRateIndex";
	
	@Autowired
	DeviceRateService deviceRateService;
	@Autowired
	DeviceService deviceService;
	@Autowired
	InvestorService investorService;
	@Autowired
	CompanyService companyService;
	@Autowired
	TraderService traderService;
	@Autowired
	SalerService salerService;
	
	@RequestMapping (method = RequestMethod.GET,path = "/index")
	public String index() {
		
		return GENERIC_DEVICE_RATE;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<DeviceRateView> page(Integer page,Integer limit,DeviceRateView params,String deviceBatchId){
		logger.debug("频率使用率监控列表请求参数对象：{}",params.toString());
		params.setDeviceBatchId(deviceBatchId);
		Page<Device> deviceRates= deviceRateService.findAllDevice(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<DeviceRateView> result = new ArrayList<>();
		for(Device deviceRate:deviceRates.getContent()){
			if(deviceRate != null){
				result.add(new DeviceRateView(deviceRate));
			}
		}
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,result,deviceRates.getTotalElements());
	}
}
