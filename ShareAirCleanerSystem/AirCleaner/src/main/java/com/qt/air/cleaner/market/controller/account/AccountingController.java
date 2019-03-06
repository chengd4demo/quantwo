package com.qt.air.cleaner.market.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("market/accounting")
public class AccountingController {
	private final Logger logger = LoggerFactory.getLogger(AccountInBoundController.class);
	private final String  ACCOUNTING_INDEX = "view/market/account/queryAccountingManagementIndex";

	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {	
		return ACCOUNTING_INDEX;
	}
}
