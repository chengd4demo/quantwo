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
import com.qt.air.cleaner.market.domain.generic.Trader;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.vo.generic.TraderView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/traders")
public class TraderController {
	private final Logger logger = LoggerFactory.getLogger(TraderController.class);
	private final String GENERIC_TRADERS_TRADER_INDEX = "view/market/generic/queryTraderIndex",
			GENERIC_TRADERS_TRADER_EDIT = "view/market/generic/editTraderIndex";
	@Autowired
	TraderService traderService;
	@Autowired
	InvestorService investorService;

	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_TRADERS_TRADER_INDEX;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<TraderView> page(Integer page,Integer limit,TraderView params){
		logger.debug("商户列表请求参数对象：{}",params.toString());
		Page<Trader> traders= traderService.findAllTrader(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<TraderView> result = new ArrayList<>();
		for(Trader trader:traders.getContent()){
			if(trader != null){
				result.add(new TraderView(trader));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, traders.getTotalElements());
	}
	@RequestMapping(method = RequestMethod.GET, path = "trader/edit")
	public String edit(String id, Model model) {
		logger.debug("商户信息查询请求参数：{}",id);
		TraderView traderView = null;
		if (StringUtils.isNotBlank(id)) {
			Trader trader = traderService.findById(id);
			if(trader != null) {
				traderView = new TraderView(trader);
				traderView.setLegend(Constants.TRADER_EDIT_LEGEND);
			}
		} else {
			traderView = new TraderView();
			traderView.setLegend(Constants.TRADER_ADD_LEGEND);
		}
		model.addAttribute("trader", traderView);
		//编辑页面下拉列表
		model.addAttribute("investors", investorService.findAll(false));
		return GENERIC_TRADERS_TRADER_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "trader/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(TraderView traderView){
		logger.debug("商户更新请求参数对象：{}",traderView.toString());
		try {
			traderService.saveOrUpdate(traderView);
		} catch (Exception e) {
			logger.error("商户更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("商户删除请求参数{}" + id);
		try {
			traderService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	@RequestMapping(method = RequestMethod.GET, path = "/list")
	@ResponseBody
	public List<TraderView> list() {
		List<Trader> traderList = traderService.findAll(false);
		List<TraderView> result = new ArrayList<>();
		for(Trader trader : traderList){
			result.add(new TraderView(trader));
		}
		return result;
	}
}
