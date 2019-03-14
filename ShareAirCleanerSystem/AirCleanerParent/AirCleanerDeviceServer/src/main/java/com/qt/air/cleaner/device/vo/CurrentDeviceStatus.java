package com.qt.air.cleaner.device.vo;

import java.io.Serializable;

public class CurrentDeviceStatus implements Serializable {
	private static final long serialVersionUID = -4998091278531367656L;
	private String machno;
	private String devicesequence;
	private Integer counts = 0;
	
	public String getMachno() {
		return machno;
	}
	public void setMachno(String machno) {
		this.machno = machno;
	}
	public String getDevicesequence() {
		return devicesequence;
	}
	public void setDevicesequence(String devicesequence) {
		this.devicesequence = devicesequence;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	
}
