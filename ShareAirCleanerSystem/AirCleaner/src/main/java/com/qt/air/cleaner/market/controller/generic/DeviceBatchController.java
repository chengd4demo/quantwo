package com.qt.air.cleaner.market.controller.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;
import com.qt.air.cleaner.market.service.generic.DeviceBatchService;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.vo.generic.DeviceBatchView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/device/batch")
public class DeviceBatchController {
	private final Logger logger = LoggerFactory.getLogger(DeviceBatchController.class);
	private final String GENERIC_DEVICE_BATCH_INDEX = "view/market/generic/queryDeviceBatchIndex",
			GENERIC_DEVICE_BATCH_EDIT = "view/market/generic/editDeviceBatchIndex";
	@Autowired
	DeviceBatchService deviceBatchService;
	@Autowired
	InvestorService investorService;

	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_DEVICE_BATCH_INDEX;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<DeviceBatchView> page(Integer page,Integer limit,DeviceBatchView params){
		Page<DeviceBatch> deviceBatchs= deviceBatchService.findAllDeviceBatch(params.getBatchNo(), params.getBatchName(), params.getInvestorId(), 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<DeviceBatchView> result = new ArrayList<>();
		for(DeviceBatch deviceBatch:deviceBatchs.getContent()){
			if(deviceBatch != null){
				result.add(new DeviceBatchView(deviceBatch));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, deviceBatchs.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id, Model model) {
		DeviceBatchView deviceBatchView = null;
		if (StringUtils.isNotBlank(id)) {
			DeviceBatch deviceBatch = deviceBatchService.findById(id);
			if(deviceBatch != null) {
				deviceBatchView = new DeviceBatchView(deviceBatch);
				deviceBatchView.setLegend(Constants.DEVICE_BATCH_EDIT_LEGEND);
			}
		} else {
			deviceBatchView = new DeviceBatchView();
			deviceBatchView.setLegend(Constants.DEVICE_BATCH_ADD_LEGEND);
		}
		model.addAttribute("deviceBatch", deviceBatchView);
		//编辑页面下拉列表
		model.addAttribute("investors", investorService.findAll(false));
		return GENERIC_DEVICE_BATCH_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(DeviceBatchView deviceBatchView){
		logger.debug("设备批次更新请求参数对象：{}",deviceBatchView.toString());
		try {
			deviceBatchService.saveOrUpdate(deviceBatchView);
		} catch (Exception e) {
			logger.error("设备批次更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("设备批次删除请求参数{}" + id);
		try {
			deviceBatchService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
}
