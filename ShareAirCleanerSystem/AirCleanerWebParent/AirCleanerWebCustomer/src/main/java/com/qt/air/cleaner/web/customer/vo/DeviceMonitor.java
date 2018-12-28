package com.qt.air.cleaner.web.customer.vo;

import java.io.Serializable;

public class DeviceMonitor implements Serializable {
	private static final long serialVersionUID = -4998091278531367656L;
	private String deviceState;
	private String machNo;
	private String costTime;
	private String lastTime;
	public String getDeviceState() {
		return deviceState;
	}
	public String getMachNo() {
		return machNo;
	}
	public String getCostTime() {
		return costTime;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}
	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}
	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
}
