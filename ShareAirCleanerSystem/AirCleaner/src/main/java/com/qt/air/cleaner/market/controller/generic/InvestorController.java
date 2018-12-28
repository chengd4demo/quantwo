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
import com.qt.air.cleaner.market.domain.generic.Investor;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.vo.generic.InvestorView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/investor")
public class InvestorController {
	private final Logger logger = LoggerFactory.getLogger(InvestorController.class);
	private final String GENERIC_INVESTOR_INDEX = "view/market/generic/queryInvestorIndex",
			GENERIC_INVESTOR_EDIT = "view/market/generic/editInvestorIndex";
	
	@Autowired
	InvestorService investorService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index(){
		return GENERIC_INVESTOR_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<InvestorView> page(Integer page,Integer limit,InvestorView params){
		logger.debug("投资商信息分页查询请求参数对象：{}",params.toString());
		Page<Investor> investors= investorService.findAllInvesttor(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<InvestorView> result = new ArrayList<>();
		for(Investor investor:investors.getContent()){
			if(investors != null){
				result.add(new InvestorView(investor));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, investors.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id,  Model model) {
		InvestorView investorView = null;
		if (StringUtils.isNotBlank(id)) {
			Investor investor = investorService.findById(id);
			if(investor != null) {
				investorView = new InvestorView(investor);
				investorView.setLegend(Constants.INVESTOR_EDIT_LEGEND);
			}
		} else {
			investorView = new InvestorView();
			investorView.setLegend(Constants.INVESTOR_ADD_LEGEND);
		}
		model.addAttribute("investor", investorView);
		return GENERIC_INVESTOR_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(InvestorView investorView){
		logger.debug("投资商信息更新请求参数对象：{}",investorView.toString());
		try {
			investorService.saveOrUpdate(investorView);
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
			investorService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/list")
	@ResponseBody
	public List<InvestorView> list() {
		List<Investor> investorList = investorService.findAll(false);
		List<InvestorView> result = new ArrayList<>();
		for(Investor investor : investorList){
			result.add(new InvestorView(investor));
		}
		return result;
	}

}
