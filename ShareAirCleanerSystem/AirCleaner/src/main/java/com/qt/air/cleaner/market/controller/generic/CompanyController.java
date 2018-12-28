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
import com.qt.air.cleaner.market.domain.generic.Company;
import com.qt.air.cleaner.market.service.generic.CompanyService;
import com.qt.air.cleaner.market.service.generic.InvestorService;
import com.qt.air.cleaner.market.vo.generic.CompanyView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;
@Controller
@RequestMapping("market/companys")
public class CompanyController {
	private final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	private final String GENERIC_COMPANYS_COMPANY_INDEX = "view/market/generic/queryCompanyIndex",
			GENERIC_COMPANYS_COMPANY_EDIT = "view/market/generic/editCompanyIndex";
	@Autowired
	CompanyService companyService;
	@Autowired
	InvestorService investorService;

	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_COMPANYS_COMPANY_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<CompanyView> page(Integer page,Integer limit,CompanyView params){
		Page<Company> companys= companyService.findAllCompany(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<CompanyView> result = new ArrayList<>();
		for(Company company:companys.getContent()){
			if(company != null){
				result.add(new CompanyView(company));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, companys.getTotalElements());
	}
	@RequestMapping(method = RequestMethod.GET, path = "company/edit")
	public String edit(String id, Model model) {
		CompanyView companyView = null;
		if (StringUtils.isNotBlank(id)) {
			Company company = companyService.findById(id);
			if(company != null) {
				companyView = new CompanyView(company);
				companyView.setLegend(Constants.COMPANY_EDIT_LEGEND);
			}
		} else {
			companyView = new CompanyView();
			companyView.setLegend(Constants.COMPANY_ADD_LEGEND);
		}
		model.addAttribute("company", companyView);
		return GENERIC_COMPANYS_COMPANY_EDIT;
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, path = "company/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(CompanyView companyView){
		logger.debug("公司信息更新请求参数对象：{}",companyView.toString());
		try {
			companyService.saveOrUpdate(companyView);
		} catch (Exception e) {
			logger.error("公司信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("公司信息删除请求参数{}" + id);
		try {
			companyService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}

}
