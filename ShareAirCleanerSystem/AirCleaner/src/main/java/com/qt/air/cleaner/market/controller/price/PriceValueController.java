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
import com.qt.air.cleaner.market.controller.generic.TraderController;
import com.qt.air.cleaner.market.domain.price.PriceValue;
import com.qt.air.cleaner.market.service.price.PriceModelService;
import com.qt.air.cleaner.market.service.price.PriceValueService;
import com.qt.air.cleaner.market.vo.price.PriceValueView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/priceValue")
public class PriceValueController {
	private final Logger logger = LoggerFactory.getLogger(TraderController.class);
	private final String PRICE_PRICE_VALUE_INDEX = "view/market/price/queryPriceValueIndex",
			PRICE_EDIT_PRICE_VALUE = "view/market/price/editPriceValue";
	@Autowired
	PriceValueService priceValueService;
	@Autowired
	PriceModelService priceModelService;

	@RequestMapping(method = RequestMethod.GET, path = "/index/{id}/{priceModelName}")
	public String index(Model model, @PathVariable("id") String modelId,@PathVariable("priceModelName") String priceModelName) {
		PriceValueView priceValue = new PriceValueView();
		priceValue.setModelId(modelId);
		model.addAttribute("priceValue", priceValue);
		model.addAttribute("priceModelName",priceModelName);
		return PRICE_PRICE_VALUE_INDEX;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<PriceValueView> page(Integer page,Integer limit,String priceModelId){
		logger.debug("指定模型价格列表请求参数对象：{}",priceModelId);
		PriceValueView params = new PriceValueView();
		params.setModelId(priceModelId);
		Page<PriceValue> traders= priceValueService.findAllPriceValue(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<PriceValueView> result = new ArrayList<>();
		for(PriceValue priceValue:traders.getContent()){
			if(priceValue != null){
				result.add(new PriceValueView(priceValue));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, traders.getTotalElements());
	}
	@RequestMapping(method = RequestMethod.GET, path = "bath/edit")
	public String edit(String priceModelName,String priceModelId, String id, Model model) {
		logger.debug("指定模型价格查询请求参数：{}",id);
		PriceValueView pricValueView = null;
		if (StringUtils.isNotBlank(id)) {
			PriceValue priceValue = priceValueService.findById(id);
			if(priceValue != null) {
				pricValueView = new PriceValueView(priceValue);
				pricValueView.setLegend(Constants.PRICE_VALUE_EDIT_LEGEND);
			}
		} else {
			pricValueView = new PriceValueView();
			pricValueView.setLegend(Constants.PRICE_VALUE_ADD_LEGEND);
		}
		pricValueView.setModelId(priceModelId);
		model.addAttribute("priceValue", pricValueView);	
		model.addAttribute("priceModelName", priceModelName);
		return PRICE_EDIT_PRICE_VALUE;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "bath/save")
	@ResponseBody
	public RetResult<Object> saveEdit(PriceValueView pricValueView){
		logger.debug("指定模型价格请求参数对象：{}",pricValueView.toString());
		try {
			priceValueService.saveOrUpdate(pricValueView);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("指定模型价格报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("指定模型价格删除请求参数{}" + id);
		try {
			priceValueService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
}
