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
import com.qt.air.cleaner.market.domain.generic.Saler;
import com.qt.air.cleaner.market.service.generic.SalerService;
import com.qt.air.cleaner.market.vo.generic.SalerView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/saler")
public class SalerController {
	private final Logger logger = LoggerFactory.getLogger(InvestorController.class);
	private final String GENERIC_SALER_INDEX = "view/market/generic/querySalerIndex",
			GENERIC_SALER_EDIT = "view/market/generic/editSalerIndex";
	
	@Autowired
	SalerService salerService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index(){
		return GENERIC_SALER_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<SalerView> page(Integer page,Integer limit,SalerView salerView){
		logger.debug("投资商信息分页查询请求参数对象：{}",salerView.toString());
		Page<Saler> saler= salerService.findAllSaler(salerView, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "joinTime"));
		List<SalerView> result = new ArrayList<>();
		for(Saler salers:saler.getContent()){
			if(salers != null){
				result.add(new SalerView(salers));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, saler.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id,  Model model) {
		SalerView salerView = null;
		if (StringUtils.isNotBlank(id)) {
			Saler saler = salerService.findById(id);
			if(saler != null) {
				salerView = new SalerView(saler);
				salerView.setLegend(Constants.SALER_EDIT_LEGEND);
			}
		} else {
			salerView = new SalerView();
			salerView.setLegend(Constants.SALER_ADD_LEGEND);
		}
		model.addAttribute("saler", salerView);
		return GENERIC_SALER_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(SalerView salerView){
		logger.debug("投资商信息更新请求参数对象：{}",salerView.toString());
		try {
			salerService.saveOrUpdate(salerView);
		} catch (Exception e) {
			logger.error("投资商信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("投资商信息删除请求参数{}" + id);
		try {
			salerService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/list")
	@ResponseBody
	public List<SalerView> list() {
		List<Saler> salerList = salerService.findAll(false);
		List<SalerView> result = new ArrayList<>();
		for(Saler saler : salerList){
			result.add(new SalerView(saler));
		}
		return result;
	}
	
}
