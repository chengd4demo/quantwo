package com.qt.air.cleaner.market.controller.price;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.qt.air.cleaner.market.domain.price.PriceModel;
import com.qt.air.cleaner.market.service.price.PriceModelService;
import com.qt.air.cleaner.market.vo.price.PriceModelView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/price")
public class PriceController {
		private final String PRICE_MODEL_INDEX = "view/market/price/queryPriceInformationIndex",
							 PRICE_MODEL_EDIT = "view/market/price/editpriceModelIndex";
//							 PRICE_VALUE_INDEX="view/market/price/queryPriceValueIndex";
						
		@Autowired
		PriceModelService priceModelService;
		@RequestMapping(method = RequestMethod.GET, path = "/priceModel")
		public String PriceModel(HttpServletRequest request, HttpServletResponse response) {
			return PRICE_MODEL_INDEX;
		}
		
//		@RequestMapping(method = RequestMethod.GET, path="/value/index/{id}")
//		public String priceValueIndex(Model model, @PathVariable("id") String priceModeId) {
//			model.addAttribute("priceModeId", priceModeId);
//			return PRICE_VALUE_INDEX;
//		}
		
		@RequestMapping(method = RequestMethod.GET, path = "/page")
		@ResponseBody
		public PageInfo<PriceModelView> page(Integer page,Integer limit,PriceModelView params){
			List<PriceModelView> result = new ArrayList<>();
			Page<PriceModel> priceModels = null;
			if(params !=null) {
				priceModels = priceModelService.findAllPriceModel(params,	new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
				for(PriceModel priceModel : priceModels) {
					result.add(new PriceModelView(priceModel));
				}
			}
			return new PageInfo<>(0, "数据读取成功", result, priceModels ==  null ? 0L : priceModels.getTotalElements());
		}	
		
		@RequestMapping(method = RequestMethod.GET, path = "/model/edit")
		public String edit(String id, Model model) {
			PriceModelView priceModelView = null;
			if (StringUtils.isNotBlank(id)) {
				PriceModel priceModel = priceModelService.findById(id);
				if(priceModel != null) {
					priceModelView = new PriceModelView(priceModel);
					priceModelView.setLegend(Constants.PRICE_MODEL_EDIT_LEGEND);
				}
			} else {
				priceModelView = new PriceModelView();
				priceModelView.setLegend(Constants.PRICE_MODEL_ADD_LEGEND);
			}
			model.addAttribute("priceModel", priceModelView);
			return PRICE_MODEL_EDIT;
		}
		
		@RequestMapping(method = RequestMethod.POST, path = "/model/save")
		@ResponseBody
		public RetResult<Object>  saveEdit(PriceModelView priceModelView){
			//logger.debug("设备批次更新请求参数对象：{}",priceModelView.toString());
			try {
				priceModelService.saveOrUpdate(priceModelView);
			} catch (Exception e) {
				//logger.error("设备批次更新报错异常:{}", e.getMessage());
				return RetResponse.makeErrRsp(e.getMessage());
			}
			return RetResponse.makeOKRsp();
		}
		
		@RequestMapping(method = RequestMethod.GET,path = "/model/del/{id}")
		@ResponseBody
		public RetResult<Object>  delete(@PathVariable("id") String id){
			//logger.debug("设备批次删除请求参数{}" + id);
			try {
				priceModelService.delete(id);
			} catch (Exception e) {
				return RetResponse.makeErrRsp(e.getMessage());
			}
			return RetResponse.makeOKRsp();
		}
		
	
		
}
