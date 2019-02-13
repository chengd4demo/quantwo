package com.qt.air.cleaner.market.controller.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qt.air.cleaner.market.service.generic.TraderService;

@Controller
@RequestMapping("market/report")
public class ReportController {
	private final String SWEEP_CODE_REPORT_INDEX = "view/market/report/querySweepCodeIndex",
			PAYMENT_RECORD_REPORT_INDEX = "view/market/report/queryPaymentRecordIndex",
			APPLY_REPORT_INDEX =  "view/market/report/queryApplyIndex";
			
	@Autowired
	TraderService traderService;
	@RequestMapping(method = RequestMethod.GET, path = "/sweeptcode/index")
	public String sweeptCodeIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return SWEEP_CODE_REPORT_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/paymentrecord/index")
	public String paymentRecordIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return PAYMENT_RECORD_REPORT_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/apply/index")
	public String applyIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return APPLY_REPORT_INDEX;
	}
}
