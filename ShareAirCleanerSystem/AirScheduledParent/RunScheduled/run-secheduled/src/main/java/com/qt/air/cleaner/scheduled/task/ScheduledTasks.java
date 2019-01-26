package com.qt.air.cleaner.scheduled.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.scheduled.service.WeiXinNotityService;

@Component
public class ScheduledTasks {
	@Autowired
	WeiXinNotityService billingService;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 报表统计
	 */
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		System.out.println("当前时间：" + dateFormat.format(new Date()));

	}

	/**
	 * 红包
	 */
	@Scheduled(fixedRate = 5000)
	public void cashCurrentTime() {

	}

	/**
	 * 对账\开账
	 */
	@Scheduled(cron = "0 0 10 * * ?")
	public void openBillingCurrentTime() {
		// 自动下载前日微信对账单
		billingService.startDownloadForSuccess(Calendar.getInstance().getTime());
		// 根据微信下载的对账单记录和通知记录进行对账处理
		billingService.updateWeiXinStatusByDownload(Calendar.getInstance().getTime());
		// 自动自行微信开帐供功能
		billingService.updateWeixinNotityForOpenAccount(Calendar.getInstance().getTime());
	}

}
