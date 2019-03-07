package com.qt.air.cleaner.market.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("market/difference")
public class DifferenceDetailsController {

	private final Logger logger = LoggerFactory.getLogger(AccountInBoundController.class);
	private final String  DIFFERENCE_INDEX = "view/market/account/queryDifferenceDetailsIndex";

	@RequestMapping(method = RequestMethod.GET,path = "/index/{id}/{orderNumber}")
	public String index() {	
		return DIFFERENCE_INDEX;
	}
}
