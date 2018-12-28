package com.qt.air.cleaner.market.controller.price;

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
import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.price.PriceSystem;
import com.qt.air.cleaner.market.service.price.PriceModelService;
import com.qt.air.cleaner.market.service.price.PriceSystemService;
import com.qt.air.cleaner.market.vo.price.PriceSystemView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/priceSystems")
public class PriceSystemController {

	private final Logger logger = LoggerFactory.getLogger(PriceSystemController.class);
	private final String GENERIC_PRICECOMPANYS_PRICESYSTEM_INDEX = "view/market/price/queryPriceSystemIndex",
			GENERIC_PRICECOMPANYS_PRICESYSTEM_EDIT = "view/market/price/editPriceSystemIndex";
	@Autowired
	PriceSystemService priceSystemService;
	@Autowired
	PriceModelService priceModelService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_PRICECOMPANYS_PRICESYSTEM_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<PriceSystemView> page(Integer page,Integer limit,PriceSystemView params){
		Page<PriceSystem> priceSystems= priceSystemService.findAllPriceSystem(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<PriceSystemView> result = new ArrayList<>();
		for(PriceSystem priceSystem:priceSystems.getContent()){
			if(priceSystem != null){
				result.add(new PriceSystemView(priceSystem));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, priceSystems.getTotalElements());
	}
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id, Model model) {
		PriceSystemView priceSystemView = null;
		if (StringUtils.isNotBlank(id)) {
			PriceSystem priceSystem = priceSystemService.findById(id);
			if(priceSystem != null) {
				priceSystemView = new PriceSystemView(priceSystem);
				priceSystemView.setLegend(Constants.PRICE_SYSTEM_EDIT_LEGEND);
			}
		} else {
			priceSystemView = new PriceSystemView();
			priceSystemView.setLegend(Constants.PRICE_SYSTEM_ADD_LEGEND);
		}
		model.addAttribute("priceSystem", priceSystemView);
		model.addAttribute("priceModels", priceModelService.findAll());
		return GENERIC_PRICECOMPANYS_PRICESYSTEM_EDIT;
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(PriceSystemView priceSystemView){
		logger.debug("价格体系管理更新请求参数对象：{}",priceSystemView.toString());
		try {
			priceSystemService.saveOrUpdate(priceSystemView);
		} catch(BusinessException e){
			return RetResponse.makeErrRsp(e.getMessage());
		} catch (Exception e) {
			logger.error("价格体系管理更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("价格体系管理信息删除请求参数{}" + id);
		try {
			priceSystemService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/affirm/{id}")
	@ResponseBody
	public RetResult<Object> priceSystemAffirm (@PathVariable("id") String id,Integer state){
		logger.debug("价格体系管理确认请求参数Id：{}",id);
		try {
			priceSystemService.updateState(id,state);
		} catch (Exception e) {
			logger.error("价格体系管理激活确认异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_9013.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
}
