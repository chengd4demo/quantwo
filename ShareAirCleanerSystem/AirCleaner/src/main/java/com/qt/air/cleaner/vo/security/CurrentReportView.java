package com.qt.air.cleaner.vo.security;

import java.io.Serializable;
import java.text.DecimalFormat;

public class CurrentReportView implements Serializable {
	private static final long serialVersionUID = 5734553828019245877L;
	private int cashApplyCount = 0;	//提现申请
	private int refundApplyCount = 0; //退款申请
	private int todayOrder = 0; //今日订单
	private int volume = 0; //成交量
	private int deviceCount = 0; //设备数量
	private int deviceOnlineCount = 0; //设备在线数量
	private int sweepCodeCount = 0; //扫码量
	private String sweepCodeRate = "0%"; //扫码率
	private String volumeRate = "0%"; //成交率
	public int getCashApplyCount() {
		return cashApplyCount;
	}
	public void setCashApplyCount(int cashApplyCount) {
		this.cashApplyCount = cashApplyCount;
	}
	public int getRefundApplyCount() {
		return refundApplyCount;
	}
	public void setRefundApplyCount(int refundApplyCount) {
		this.refundApplyCount = refundApplyCount;
	}
	public int getTodayOrder() {
		return todayOrder;
	}
	public void setTodayOrder(int todayOrder) {
		this.todayOrder = todayOrder;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}
	public int getDeviceOnlineCount() {
		return deviceOnlineCount;
	}
	public void setDeviceOnlineCount(int deviceOnlineCount) {
		this.deviceOnlineCount = deviceOnlineCount;
	}
	public int getSweepCodeCount() {
		return sweepCodeCount;
	}
	public void setSweepCodeCount(int sweepCodeCount) {
		this.sweepCodeCount = sweepCodeCount;
	}
	public String getSweepCodeRate() {
		Float sweepCodeRate = (Float.parseFloat(String.valueOf(this.sweepCodeCount)) / Float.parseFloat(String.valueOf(this.deviceCount)))*100.0f;
		return new DecimalFormat(".00").format(sweepCodeRate) + "%";
	}
	public void setSweepCodeRate(String sweepCodeRate) {
		this.sweepCodeRate = sweepCodeRate;
	}
	public String getVolumeRate() {
		Float volumeRate = (Float.parseFloat(String.valueOf(this.volume)) / Float.parseFloat(String.valueOf(this.todayOrder)))*100.0f;
		return  new DecimalFormat(".00").format(volumeRate) + "%";
	}
	public void setVolumeRate(String volumeRate) {
		this.volumeRate = volumeRate;
	}
}
