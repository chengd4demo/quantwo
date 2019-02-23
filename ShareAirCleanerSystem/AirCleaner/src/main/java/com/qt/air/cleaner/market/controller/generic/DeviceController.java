package com.qt.air.cleaner.market.controller.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.service.generic.CompanyService;
import com.qt.air.cleaner.market.service.generic.DeviceService;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.service.generic.SalerService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.service.price.PriceSystemService;
import com.qt.air.cleaner.market.vo.generic.DeviceView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/devices")
public class DeviceController {

	private final Logger logger = LoggerFactory.getLogger(DeviceController.class);
	private final String GENERIC_DEVICES_DEVICE_INDEX = "view/market/generic/queryDeviceIndex",
			GENERIC_DEVICES_DEVICE_EDIT = "view/market/generic/editDeviceIndex",
			GENERIC_DEVICE_QRCODE = "view/market/generic/viewDeviceQRcodeIndex",
			GENERIC_SHARE_PROFILE = "view/market/generic/editNewShareProfile";
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
	@Autowired
	PriceSystemService priceSystemService;
	
	@Value("${file.uploadFolder.qrcode}")
	public String DEVICE_QRCODE_CREATE_PATH;
	
	@RequestMapping (method = RequestMethod.GET,path = "/add")
	public String add() {
		
		return GENERIC_SHARE_PROFILE;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/index/{id}/{deviceBatchName}")
	public String index(Model model, @PathVariable("id") String deviceBatchId,@PathVariable("deviceBatchName") String deviceBatchName) {
		DeviceView deviceView = new DeviceView();
		deviceView.setDeviceBatchId(deviceBatchId);
		deviceView.setDeviceBatchName(deviceBatchName);	
		model.addAttribute("device", deviceView);
		model.addAttribute("investors", investorService.findAll(false));
		model.addAttribute("traders", traderService.findAll(false));
		model.addAttribute("salers", salerService.findAll(false));
		return GENERIC_DEVICES_DEVICE_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<DeviceView> page(Integer page,Integer limit,DeviceView params,String deviceBatchId){
		logger.debug("设备列表请求参数对象：{}",params.toString());
		params.setDeviceBatchId(deviceBatchId);
		Page<Device> devices= deviceService.findAllDevice(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<DeviceView> result = new ArrayList<>();
		for(Device device:devices.getContent()){
			if(device != null){
				result.add(new DeviceView(device));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, devices.getTotalElements());
	}
	@RequestMapping(method = RequestMethod.GET, path = "device/edit")
	public String edit(String id, String deviceBatchId,Model model) {
		logger.debug("设备信息查询请求参数：{}",id);
		DeviceView deviceView = null;
		model.addAttribute("investors", investorService.findAll(false));
		model.addAttribute("traders", traderService.findAll(false));
		model.addAttribute("companys", companyService.findAll(false));
		model.addAttribute("salers", salerService.findAll(false));
		model.addAttribute("priceSystems", priceSystemService.findAll(false));
		if (StringUtils.isNotBlank(id)) {
			Device device = deviceService.findById(id);
			if(device != null) {
				deviceView = new DeviceView(device);
				deviceView.setLegend(Constants.DEVICE_EDIT_LEGEND);
			}
		} else {
			deviceView = new DeviceView();
			deviceView.setLegend(Constants.DEVICE_ADD_LEGEND);
		}
		deviceView.setDeviceBatchId(deviceBatchId);
		model.addAttribute("device", deviceView);
		return GENERIC_DEVICES_DEVICE_EDIT;
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, path = "device/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(DeviceView deviceView){
		logger.debug("设备信息更新请求参数对象：{}",deviceView.toString());
		try {
			deviceService.saveOrUpdate(deviceView);
		} catch (Exception e) {
			logger.error("设备信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/device/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("设备信息删除请求参数{}" + id);
		try {
			deviceService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/view/qrcode")
	public String viewQRcode(String machNo,String deviceSequence, Model model) {
		
		String qrcode = deviceService.generateQRcode(deviceSequence,
				DEVICE_QRCODE_CREATE_PATH);
		model.addAttribute("qrcode", qrcode);
		model.addAttribute("title", "S/N:"+deviceSequence+"");
		return GENERIC_DEVICE_QRCODE;
	}
	
	@RequestMapping(method = RequestMethod.POST,path = "/batchImport/{deviceBatchId}")
	@ResponseBody
	public RetResult<Object>  batchImport(@RequestParam MultipartFile file,@PathVariable("deviceBatchId") String deviceBatchId){
		try {
			if(StringUtils.isBlank(deviceBatchId)) 
				return RetResponse.makeErrRsp(ErrorCodeEnum.ES_8001.getMessage());
			if(file == null)
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_5001.getMessage());
			// 获取文件名
			String name = file.getOriginalFilename();
			// 判断文件大小、即名称
			long size = file.getSize();
			if (name == null || ("").equals(name) && size == 0)
				return RetResponse.makeErrRsp(ErrorCodeEnum.ES_6001.getMessage());

			int result = deviceService.batchImport(deviceBatchId,name,file);
			if (result == 0) {
				return RetResponse.makeErrRsp(ErrorCodeEnum.ES_7001.getMessage());
			}
		} catch(ConstraintViolationException e){
			logger.error("设备信息批量导入异常：错误编码["+ErrorCodeEnum.ES_9010.getErrorCode()+"];"+"错误信息：["+ErrorCodeEnum.ES_9010.getMessage()+"]");
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_9010.getMessage());
		}catch (Exception e) {
			logger.error("设备信息批量导入异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_1001.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
}
