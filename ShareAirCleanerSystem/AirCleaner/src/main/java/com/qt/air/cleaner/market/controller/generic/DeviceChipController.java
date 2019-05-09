package com.qt.air.cleaner.market.controller.generic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.generic.DeviceChip;
import com.qt.air.cleaner.market.domain.generic.Investor;
import com.qt.air.cleaner.market.domain.generic.Saler;
import com.qt.air.cleaner.market.domain.generic.Trader;
import com.qt.air.cleaner.market.service.generic.DeviceChipService;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.service.generic.SalerService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/device/chip")
public class DeviceChipController {

	private final Logger logger = LoggerFactory.getLogger(DeviceChipController.class);
	private final String GENERIC_DEVICE_CHIP = "view/market/generic/queryDeviceChipIndex";
	
	@Autowired
	DeviceChipService deviceChipService;
	@Autowired
	TraderService traderService;
	@Autowired
	InvestorService investorService;
	@Autowired
	SalerService salerService;
	
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
	
	@RequestMapping (method = RequestMethod.GET,path = "/updateChangeTime/{machNo}")
	@ResponseBody
	public RetResult<Object> updateChangeTime (@PathVariable("machNo") String machNo){
		logger.debug("滤芯监控事件恢复请求参数machNo：{}",machNo);
		try {
			deviceChipService.updateDeviceChip(machNo);
		} catch (Exception e) {
			logger.error("入账确认异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_4001.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/trader/list")
	@ResponseBody
	public List<Trader> traderList() {
		List<Trader> traderList = traderService.findAll(false);
		return traderList;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/investor/list")
	@ResponseBody
	public List<Investor> investorList() {
		List<Investor> investorList = investorService.findAll(false);
		return investorList;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/saler/list")
	@ResponseBody
	public List<Saler> salerList() {
		List<Saler> salerList = salerService.findAll(false);
		return salerList;
	}
}
