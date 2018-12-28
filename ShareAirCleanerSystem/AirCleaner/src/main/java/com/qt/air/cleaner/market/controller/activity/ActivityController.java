package com.qt.air.cleaner.market.controller.activity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("market/activity/main")
public class ActivityController {

	private final String  ACTIVITY_INDEX = "view/market/activity/activityIndex";
	
	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {	
		return ACTIVITY_INDEX;
	}
	
}
