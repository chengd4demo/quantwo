package com.qt.air.cleaner.device.vo;

import java.io.Serializable;

public class DeviceMonitor implements Serializable{
	private static final long serialVersionUID = -4998091278531367656L;
	private String deviceid;
	private String devicestate;
	private String tradername;
	private String machno;
	private String usedate;
	private String address;
	private Integer costtime = 0;
	private Integer lasttime = 0;
	private Float unitprice = 0.00f;
	private String devicesequence;
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getDevicestate() {
		return devicestate;
	}
	public void setDevicestate(String devicestate) {
		this.devicestate = devicestate;
	}
	public String getTradername() {
		return tradername;
	}
	public void setTradername(String tradername) {
		this.tradername = tradername;
	}
	public String getMachno() {
		return machno;
	}
	public void setMachno(String machno) {
		this.machno = machno;
	}
	public String getUsedate() {
		return usedate;
	}
	public void setUsedate(String usedate) {
		this.usedate = usedate;
	}
	
	public Integer getCosttime() {
		return costtime/60;
	}
	public void setCosttime(Integer costtime) {
		this.costtime = costtime;
	}
	public Integer getLasttime() {
		return lasttime/60;
	}
	public void setLasttime(Integer lasttime) {
		this.lasttime = lasttime;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDevicesequence() {
		return devicesequence;
	}
	public void setDevicesequence(String devicesequence) {
		this.devicesequence = devicesequence;
	}
	
	
	
}
