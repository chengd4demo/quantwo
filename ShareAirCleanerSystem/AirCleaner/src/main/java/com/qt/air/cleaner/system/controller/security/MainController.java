package com.qt.air.cleaner.system.controller.security;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.system.service.security.CurrentReportService;
import com.qt.air.cleaner.vo.security.CurrentReportView;


@Controller
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	@Autowired
	CurrentReportService currentReportService;
    @RequestMapping(value = { "/", "index" }, method = RequestMethod.GET)
    String home() {
        log.info("# 进入默认首页");
        return "view/index";
    }

    @RequestMapping(value = "leftnav", method = RequestMethod.GET)
    String leftnav() {
        return "leftnav";
    }

    @RequestMapping(value = "topnav", method = RequestMethod.GET)
    String topnav() {
        return "topnav";
    }
    
    @RequestMapping("welcome")
	public String defaultShowPage() {
		return "system/welcome";
	}
    
    @RequestMapping("current/report")
    @ResponseBody
    public CurrentReportView currentReport() {
    	CurrentReportView currentReport = new CurrentReportView();
    	currentReport.setCashApplyCount(currentReportService.findCashApplyCount(Calendar.getInstance().getTime()));
    	currentReport.setRefundApplyCount(currentReportService.findRefundApplyCount(Calendar.getInstance().getTime()));
    	currentReport.setTodayOrder(currentReportService.findTodayOrder(Calendar.getInstance().getTime()));
    	currentReport.setVolume(currentReportService.findVolume(Calendar.getInstance().getTime()));
    	currentReport.setDeviceCount(currentReportService.findDeviceCount(Calendar.getInstance().getTime()));
    	currentReport.setDeviceOnlineCount(currentReportService.findDeviceOnlineCount(Calendar.getInstance().getTime()));
    	currentReport.setSweepCodeCount(currentReportService.findSweepCodeCount(Calendar.getInstance().getTime()));
    	return currentReport;
    	
    }

}
