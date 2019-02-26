package com.qt.air.cleaner.market.controller.generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("market/agent")
public class AgentController {
	private final Logger logger = LoggerFactory.getLogger(AgentController.class);
	private final String GENERIC_COMPANYS_COMPANY_INDEX = "view/market/generic/queryAgentIndex",
			GENERIC_COMPANYS_COMPANY_EDIT = "view/market/generic/editAgentIndex";
	
}
