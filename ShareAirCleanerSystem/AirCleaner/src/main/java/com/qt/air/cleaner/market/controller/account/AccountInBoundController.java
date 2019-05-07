package com.qt.air.cleaner.market.controller.account;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.account.AccountInBound;
import com.qt.air.cleaner.market.service.account.AccountInBoundService;
import com.qt.air.cleaner.market.vo.account.AccountInBoundView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/accountInBound")
public class AccountInBoundController {
	private final Logger logger = LoggerFactory.getLogger(AccountInBoundController.class);
	private final String  ACCOUNT_ACCOUNTINBOUND_INDEX = "view/market/account/queryAccountInBoundIndex";
	
	
	@Autowired
	AccountInBoundService accountInBoundService;
	
	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {	
		return ACCOUNT_ACCOUNTINBOUND_INDEX;
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/page")
	@ResponseBody
	public PageInfo<AccountInBoundView> page(Integer page,Integer limit,AccountInBoundView accountInBoundView){
		logger.debug("设备消费记录分页查询请求参数对象：{}",accountInBoundView.toString());
		Page<AccountInBound> accountInBounds = accountInBoundService.findAllAccountInBound(accountInBoundView, new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<AccountInBoundView> result = new ArrayList<>();
		for(AccountInBound accountInBound:accountInBounds.getContent()) {
			if(accountInBounds!=null) {
				result.add(new AccountInBoundView (accountInBound));
			}
		}
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,result,accountInBounds.getTotalElements());	
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/affirm/{id}")
	@ResponseBody
	public RetResult<Object> inBoundAffirm (@PathVariable("id") String id){
		logger.debug("入账确认请求参数Id：{}",id);
		try {
			accountInBoundService.updateState(id);
		} catch (Exception e) {
			logger.error("入账确认异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_4001.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping (method = RequestMethod.POST,path = "/batch/affirm")
	@ResponseBody
	public RetResult<Object> inBoundAffirm (HttpServletRequest reuqust){
		String[] ids = reuqust.getParameterValues("ids[]");
		try {
			if (ids == null || ids.length ==0) {
				return RetResponse.makeErrRsp(ErrorCodeEnum.ES_4001.getMessage());
			} else {
				accountInBoundService.updateBatchAffirm(ids);
			}
			
		} catch (Exception e) {
			logger.error("入账确认异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_4001.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	
	
}
