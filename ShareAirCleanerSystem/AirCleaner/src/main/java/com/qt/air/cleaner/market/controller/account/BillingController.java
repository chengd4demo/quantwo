package com.qt.air.cleaner.market.controller.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.account.Billing;
import com.qt.air.cleaner.market.service.account.BillingService;
import com.qt.air.cleaner.market.vo.account.BillingView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/billing")
public class BillingController {
	private final Logger logger = LoggerFactory.getLogger(BillingController.class);
	private final String  ACCOUNT_BILLING_INDEX = "view/market/account/queryBillingIndex";
	
	
	@Autowired
	BillingService billingService;
	
	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {	
		return ACCOUNT_BILLING_INDEX;
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/page")
	@ResponseBody
	public PageInfo<BillingView> page(Integer page,Integer limit,BillingView billingView){
		logger.debug("设备消费记录分页查询请求参数对象：{}",billingView.toString());
		Page<Billing> billings = billingService.findAllBilling(billingView, new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<BillingView> result = new ArrayList<>();
		for(Billing billing:billings.getContent()) {
			if(billings!=null) {
				result.add(new BillingView(billing));
			}
		}
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,result,billings.getTotalElements());		
	}
	
}
