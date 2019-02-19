package com.qt.air.cleaner.system.service.security;

import java.util.Date;

public interface CurrentReportService {

	/**
	 * 提现申请
	 * 
	 * @param time
	 * @return
	 */
	int findCashApplyCount(Date time);

	/**
	 * 退款申请
	 * 
	 * @param time
	 * @return
	 */
	int findRefundApplyCount(Date time);

	/**
	 * 今日订单
	 * 
	 * @param time
	 * @return
	 */
	int findTodayOrder(Date time);

	/**
	 * 成交量
	 * 
	 * @param time
	 * @return
	 */
	int findVolume(Date time);

	/**
	 * 设备数量
	 * 
	 * @param time
	 * @return
	 */
	int findDeviceCount(Date time);

	/**
	 * 设备在线数量
	 * 
	 * @param time
	 * @return
	 */
	int findDeviceOnlineCount(Date time);

	/**
	 * 扫码量
	 * 
	 * @param time
	 * @return
	 */
	int findSweepCodeCount(Date time);



}
