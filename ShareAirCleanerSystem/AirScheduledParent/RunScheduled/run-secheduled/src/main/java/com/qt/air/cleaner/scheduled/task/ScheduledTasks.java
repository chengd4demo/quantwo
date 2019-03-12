package com.qt.air.cleaner.scheduled.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.scheduled.service.CashWithdrawalService;
import com.qt.air.cleaner.scheduled.service.ReportBillingService;
import com.qt.air.cleaner.scheduled.service.WeiXinNotityService;

@Component
public class ScheduledTasks {
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
	 * 报表统计
	 */
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		//System.out.println("当前时间：" + dateFormat.format(new Date()));
		//reportBillingService.startReportBilling(Calendar.getInstance().getTime());
	}

	/**
	 * 红包发送
	 */
	@Scheduled(fixedRate = 5000)
	public void sendCashCurrentTime() {
		//cashWithdrawalService.sendRedWithdrawal();
		System.out.println(withholdTaxes);
	}
	
	/**
	 * 红包状态更新
	 */
//	@Scheduled(cron = "0 0/10 * * * ?")
	@Scheduled(fixedRate = 6000)
	public void updateRedWithdrawalState() {
		cashWithdrawalService.updateRedWithdrawalState();
	}

	/**
	 * 对账\开账
	 */
	@Scheduled(cron = "0 0 10 * * ?")
//	@Scheduled(fixedRate = 5000)
	public void openBillingCurrentTime() {
		// 自动下载前日微信对账单
		billingService.startDownloadForSuccess(Calendar.getInstance().getTime());
//		 根据微信下载的对账单记录和通知记录进行对账处理
		billingService.updateWeiXinStatusByDownload(Calendar.getInstance().getTime());
//		 自动自行微信开帐供功能
		billingService.updateWeixinNotityForOpenAccount(Calendar.getInstance().getTime());
	}

}
