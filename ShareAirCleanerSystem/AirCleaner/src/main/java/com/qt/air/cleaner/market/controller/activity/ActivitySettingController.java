package com.qt.air.cleaner.market.controller.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.service.activity.ActivitySetService;
import com.qt.air.cleaner.system.domain.config.SysConfig;
import com.qt.air.cleaner.system.repository.config.ConfigRepository;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/activity/set")
public class ActivitySettingController {
	@Autowired
	ConfigRepository configRepository;
	@Autowired
	ActivitySetService activitySetService;

	private final String ACTIVITY_QUERYACTIVITY_INDEX = "view/market/activity/queryActivityIndex";

	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index(Model model) {
		SysConfig config = configRepository.findByBussinessId(Constants.ACTIVITY_CONFIG_BUSSINESSID);
		model.addAttribute("config", config);
		return ACTIVITY_QUERYACTIVITY_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/open")
	@ResponseBody
	public RetResult<Object> saveEdit(Boolean on){
		try {
			activitySetService.updateConfig(on, Constants.ACTIVITY_CONFIG_BUSSINESSID);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_9012.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
}
