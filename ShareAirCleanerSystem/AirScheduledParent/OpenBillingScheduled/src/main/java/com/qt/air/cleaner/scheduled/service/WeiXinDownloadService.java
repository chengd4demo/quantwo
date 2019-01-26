package com.qt.air.cleaner.scheduled.service;

import java.util.Date;

import com.qt.air.cleaner.scheduled.domain.BillingSuccess;

public interface WeiXinDownloadService {
	public void startDownloadForSuccess(Date currentTime);

	public BillingSuccess queryBillingSuccess(String transactionId, String outTradeNo, String deviceInfo,Float totalFee);
}
