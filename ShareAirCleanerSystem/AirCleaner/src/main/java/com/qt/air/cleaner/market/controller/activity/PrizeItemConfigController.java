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
import com.qt.air.cleaner.market.domain.activity.PrizeItemConfig;
import com.qt.air.cleaner.market.service.activity.PrizeItemConfigService;
import com.qt.air.cleaner.market.vo.activity.PrizeItemConfigView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/prizeItem")
public class PrizeItemConfigController {
	
	private final Logger logger = LoggerFactory.getLogger(PrizeItemConfigController.class);
	private final String  ACTIVITY_PRIZEITEM_INDEX = "view/market/activity/queryPrizeItemIndex",
			ACTIVITY_PRIZEITEM_EDIT = "view/market/activity/editPrizeItemIndex";
	@Autowired
	PrizeItemConfigService prizeItemConfigService;
	
	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {	
		return ACTIVITY_PRIZEITEM_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<PrizeItemConfigView> page(Integer page,Integer limit,PrizeItemConfigView params){
		logger.debug("奖项设置分页查询请求参数对象：{}",params.toString());
		Page<PrizeItemConfig> prizeItemConfigs= prizeItemConfigService.findAllPrizeItem(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<PrizeItemConfigView> result = new ArrayList<>();
		for(PrizeItemConfig prizeItemConfig:prizeItemConfigs.getContent()){
			if(prizeItemConfigs != null){
				result.add(new PrizeItemConfigView(prizeItemConfig));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, prizeItemConfigs.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id,  Model model) {
		PrizeItemConfigView prizeItemConfigView = null;
		if (StringUtils.isNotBlank(id)) {
			PrizeItemConfig prizeItemConfig = prizeItemConfigService.findById(id);
			if(prizeItemConfig != null) {
				prizeItemConfigView = new PrizeItemConfigView(prizeItemConfig);
				prizeItemConfigView.setLegend(Constants.PRIZE_ITEM_EDIT_LEGEND);
			}
		} else {
			prizeItemConfigView = new PrizeItemConfigView();
			prizeItemConfigView.setLegend(Constants.PRIZE_ITEM_ADD_LEGEND);
		}
		model.addAttribute("prizeItemConfig", prizeItemConfigView);
		return ACTIVITY_PRIZEITEM_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(PrizeItemConfigView prizeItemConfigView){
		logger.debug("奖项设置更新请求参数对象：{}",prizeItemConfigView.toString());
		try {
			prizeItemConfigService.saveOrUpdate(prizeItemConfigView);
		} catch (Exception e) {
			logger.error("奖项设置更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("奖项设置删除请求参数{}" + id);
		try {
			prizeItemConfigService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	

}
