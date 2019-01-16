package com.qt.air.cleaner.scheduled.service;

import java.util.Date;

public interface WeiXinDownloadService {
	/**
	 *  自动下载前日微信对账单
	 * 
	 * @param billDate
	 */
	void startDownloadForSuccess(Date billDate);
	
	/**
	 * 根据微信下载的对账单记录和通知记录进行对账处理
	 * 
	 * @param billDate
	 */
	void startDownloadForRefund(Date billDate);
	
}
