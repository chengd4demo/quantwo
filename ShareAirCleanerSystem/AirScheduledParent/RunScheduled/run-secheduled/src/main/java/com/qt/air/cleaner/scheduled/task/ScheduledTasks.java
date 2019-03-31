package com.qt.air.cleaner.scheduled.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.scheduled.service.CashWithdrawalService;
import com.qt.air.cleaner.scheduled.service.ReportBillingService;
import com.qt.air.cleaner.scheduled.service.WeiXinNotityService;

@Component
public class ScheduledTasks {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	WeiXinNotityService billingService;
	@Autowired
	ReportBillingService reportBillingService;
	@Autowired
	CashWithdrawalService cashWithdrawalService;
	@Value("${cash.withhold.taxes}")
	public Float withholdTaxes;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 报表统计 启动执行一次,然后每5s执行
	 * 
	 */
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		logger.info("开始执报表统计任务：当前系统时间：{}", dateFormat.format(new Date()));
		 reportBillingService.startReportBilling(Calendar.getInstance().getTime());
	}

	/**
	 * 红包发送 启动后延迟30秒执行,每60秒执行一次
	 * 
	 */
	@Scheduled(initialDelay=30000,fixedRate = 60000)
	public void sendCashCurrentTime() {
		logger.info("开始执微信发送红包任务：当前系统时间：{}", dateFormat.format(new Date()));
		 cashWithdrawalService.sendRedWithdrawal();
	}

	/**
	 * 红包状态更新 启动后延迟60秒执行,每15秒执行一次
	 * 
	 */
	@Scheduled(initialDelay=10000,fixedRate = 15000)
	public void updateRedWithdrawalState() {
		logger.info("开始执红包状态更新任务：当前系统时间：{}", dateFormat.format(new Date()));
		 cashWithdrawalService.updateRedWithdrawalState();
	}

	/**
	 * 对账\开账 每天早上十点执行
	 * 
	 */
	 @Scheduled(cron = "0 0 10 * * ?")
	public void openBillingCurrentTime() {
		logger.info("开始执行自动开帐任务：当前系统时间：{}", dateFormat.format(new Date()));
		 //自动下载前日微信对账单
		 billingService.startDownloadForSuccess(Calendar.getInstance().getTime());
		 //根据微信下载的对账单记录和通知记录进行对账处理
		 billingService.updateWeiXinStatusByDownload(Calendar.getInstance().getTime());
		 //自动自行微信开帐供功能
		 billingService.updateWeixinNotityForOpenAccount(Calendar.getInstance().getTime());
	}

}
