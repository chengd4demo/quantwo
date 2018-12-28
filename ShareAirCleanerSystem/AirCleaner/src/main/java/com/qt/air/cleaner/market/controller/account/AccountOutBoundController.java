package com.qt.air.cleaner.market.controller.account;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.account.AccountOutBound;
import com.qt.air.cleaner.market.service.account.AccountOutBoundService;
import com.qt.air.cleaner.market.vo.account.AccountOutBoundView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/accountOutBound")
public class AccountOutBoundController {

	private final Logger logger = LoggerFactory.getLogger(AccountOutBoundController.class);
	private final String GENERIC_ACCOUNTOUTBOUND_INDEX = "view/market/account/queryAccountOutBoundIndex",
			 GENERIC_ACCOUNTSYSTEM_INDEX = "view/market/account/queryAccountSystemIndex",
			 GENERIC_ACCOUNTSYSTEM_EDIT = "view/market/account/editAccountSystemIndex";
	@Autowired
	AccountOutBoundService accountOutBoundService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_ACCOUNTOUTBOUND_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/indexs")
	public String indexs() {
		return GENERIC_ACCOUNTSYSTEM_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<AccountOutBoundView> page(Integer page,Integer limit,AccountOutBoundView accountOutBoundView){
		logger.debug("出账记录分页查询请求参数对象：{}",accountOutBoundView.toString());
		String method = accountOutBoundView.getMethod();
		Page<AccountOutBound> accountOutBounds= accountOutBoundService.findAllAccountOutBound(accountOutBoundView, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, StringUtils.equals(method, Constants.ACCOUNT_OUT_BOUND_METHOD_RECORD) ? "lastOperateTime" : "createTime"));
		List<AccountOutBoundView> result = new ArrayList<>();
		for(AccountOutBound accountOutBound:accountOutBounds.getContent()){
			if(accountOutBound != null){
				result.add(new AccountOutBoundView(accountOutBound));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, accountOutBounds.getTotalElements());
	}
	@RequestMapping(method = RequestMethod.GET, path = "audit/edit")
	public String edit(String id, Model model) {
		logger.debug("提现管理请求参数：{}",id);
		AccountOutBound result = accountOutBoundService.findByIdRemoved(id,false);
		AccountOutBoundView accountOutBound = new AccountOutBoundView();
		accountOutBound.setId(result.getId());
		accountOutBound.setRejectReasonId(result.getOutBoundRejectReason().getId());
		model.addAttribute("outBound",accountOutBound);
		return GENERIC_ACCOUNTSYSTEM_EDIT;
	}
		
	@RequestMapping(method = RequestMethod.POST, path = "audit/edit/examine")
	@ResponseBody
	public RetResult<Object>  saveEdit(AccountOutBoundView accountOutBoundView){
		String id = accountOutBoundView.getId();
		logger.debug("提现管理更新请求参数对象：{}",accountOutBoundView.toString());
		if(accountOutBoundView == null || StringUtils.isEmpty(id)){
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_4001.getMessage());
		}
		try {
			accountOutBoundService.updateState(accountOutBoundView);
		} catch (Exception e) {
			logger.error("提现管理更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}	
}
