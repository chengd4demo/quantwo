package com.qt.air.cleaner.market.controller.platform;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.service.platform.PlatformSetService;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/platform")
public class PlatformSetController {
	private final Logger logger = LoggerFactory.getLogger(PlatformSetController.class);
	private final String PLATFORM_SHAREPROFIT_EDIT = "view/market/platform/editShareProfile";
	@Autowired
	PlatformSetService platformSetService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/shareprofit/edit")
	public String index(Model model) {
		List<ShareProfit> shareProfits = platformSetService.findAll();
		model.addAttribute("shareProfits", shareProfits);
		if(!CollectionUtils.isEmpty(shareProfits)) {
			model.addAttribute("free", shareProfits.get(0).getFree());
		}
		
		return PLATFORM_SHAREPROFIT_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/shareprofit/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(@RequestBody List<ShareProfit> shareProfit){
		logger.debug("平台设定信息更新请求参数对象：{}",shareProfit.toString());
		try {
			platformSetService.save(shareProfit);
		} catch (Exception e) {
			logger.error("投资商信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
}
