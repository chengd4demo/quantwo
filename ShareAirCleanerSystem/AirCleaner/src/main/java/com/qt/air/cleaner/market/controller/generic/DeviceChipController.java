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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.service.generic.CompanyService;
import com.qt.air.cleaner.market.service.generic.DeviceChipService;
import com.qt.air.cleaner.market.service.generic.DeviceService;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.service.generic.SalerService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/device/chip")
public class DeviceChipController {

	private final Logger logger = LoggerFactory.getLogger(DeviceChipController.class);
	private final String GENERIC_DEVICE_CHIP = "view/market/generic/queryDeviceChipIndex";
	
	@Autowired
	DeviceChipService deviceChipService;
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
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index(Model model) {
		DeviceChipView deviceChipView = new DeviceChipView();
		
		model.addAttribute("device", deviceChipView);
		model.addAttribute("investors", investorService.findAll(false));
		model.addAttribute("traders", traderService.findAll(false));
		model.addAttribute("salers", salerService.findAll(false));
		return GENERIC_DEVICE_CHIP;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<DeviceChipView> page(Integer page,Integer limit,DeviceChipView params,String deviceBatchId){
		logger.debug("设备滤芯监控列表请求参数对象：{}",params.toString());
		params.setDeviceBatchId(deviceBatchId);
		Page<Device> deviceChips = deviceChipService.findAllDevice(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<DeviceChipView> result = new ArrayList<>();
		for(Device deviceChip:deviceChips.getContent()){
			if(deviceChip != null){
				result.add(new DeviceChipView(deviceChip));
			}
		}
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,result,deviceChips.getTotalElements());
	}
}
