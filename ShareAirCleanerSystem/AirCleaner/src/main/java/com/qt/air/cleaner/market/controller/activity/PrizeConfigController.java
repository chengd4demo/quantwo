package com.qt.air.cleaner.market.controller.activity;

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
import com.qt.air.cleaner.market.domain.activity.PrizeConfig;
import com.qt.air.cleaner.market.service.activity.PrizeConfigService;
import com.qt.air.cleaner.market.service.activity.PrizeItemConfigService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.vo.activity.PrizeConfigView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/prize")
public class PrizeConfigController {

	private final Logger logger = LoggerFactory.getLogger(PrizeConfigController.class);
	private final String  ACTIVITY_PRIZE_INDEX = "view/market/activity/queryPrizeIndex",
			ACTIVITY_PRIZE_EDIT = "view/market/activity/editPrizeIndex";
	@Autowired
	PrizeConfigService prizeConfigService;
	@Autowired
	PrizeItemConfigService prizeItemConfigService;
	@Autowired
	TraderService traderService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index(Model model) {
		PrizeConfigView prizeConfigView = new PrizeConfigView();	
		model.addAttribute("prizeConfig", prizeConfigView);
		model.addAttribute("prizeItemConfigs", prizeItemConfigService.findAll(false));
		model.addAttribute("traders", traderService.findAll(false));
		return ACTIVITY_PRIZE_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<PrizeConfigView> page(Integer page,Integer limit,PrizeConfigView params){
		logger.debug("奖品设置分页查询请求参数对象：{}",params.toString());
		Page<PrizeConfig> prizeConfigs= prizeConfigService.findAllPrize(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<PrizeConfigView> result = new ArrayList<>();
		for(PrizeConfig prizeConfig:prizeConfigs.getContent()){
			if(prizeConfigs != null){
				result.add(new PrizeConfigView(prizeConfig));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, prizeConfigs.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id,  Model model) {
		PrizeConfigView prizeConfigView = null;
		model.addAttribute("prizeItemConfigs", prizeItemConfigService.findAll(false));
		model.addAttribute("traders", traderService.findAll(false));
		if (StringUtils.isNotBlank(id)) {
			PrizeConfig prizeConfig = prizeConfigService.findById(id);
			if(prizeConfig != null) {
				prizeConfigView = new PrizeConfigView(prizeConfig);
				prizeConfigView.setLegend(Constants.PRIZE_EDIT_LEGEND);
			}
		} else {
			prizeConfigView = new PrizeConfigView();
			prizeConfigView.setLegend(Constants.PRIZE_ADD_LEGEND);
		}
		model.addAttribute("prizeConfig", prizeConfigView);
		return ACTIVITY_PRIZE_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(PrizeConfigView prizeConfigView){
		logger.debug("奖品设置更新请求参数对象：{}",prizeConfigView.toString());
		try {
			prizeConfigService.saveOrUpdate(prizeConfigView);
		} catch (Exception e) {
			logger.error("奖品设置更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("奖品设置删除请求参数{}" + id);
		try {
			prizeConfigService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/affirm/{id}")
	@ResponseBody
	public RetResult<Object> prizeAffirm (@PathVariable("id") String id,String state){
		logger.debug("奖品激活确认请求参数Id：{}",id);
		try {
			prizeConfigService.updateState(id,state);
		} catch (Exception e) {
			logger.error("奖品激活确认异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_9013.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
}
