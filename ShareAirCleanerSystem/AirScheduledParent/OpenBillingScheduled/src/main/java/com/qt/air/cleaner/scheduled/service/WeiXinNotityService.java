package com.qt.air.cleaner.scheduled.service;

import java.util.Date;

public interface WeiXinNotityService {

	/**
	 * 自动下载前日微信对账单
	 * 
	 * @param currentTime
	 */
	public void startDownloadForSuccess(Date currentTime);
	
	/**
	 * 根据微信下载的对账单记录和通知记录进行对账处理
	 * 
	 * @param currentTime
	 */
	public void updateWeiXinStatusByDownload(Date currentTime);
	
	/**
	 * 微信开帐供功能
	 * 
	 * @param currentTime
	 */
	public void updateWeixinNotityForOpenAccount(Date currentTime);
	
	
}
