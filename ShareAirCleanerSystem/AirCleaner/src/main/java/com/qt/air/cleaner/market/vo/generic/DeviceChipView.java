package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;

public class DeviceChipView implements Serializable{
	private static final long serialVersionUID = -2345267931244086323L;
	private String machno; //设备编号
	private String devicesequence; //序列号
	private String batchname; //设备批次名
	private String investorid; //投资商Id
	private String traderid; //商家Id
	private String salerid; //促销员Id
	private String type;//warning all
	@Override
	public String toString() {
		return "DeviceChipView [machno=" + machno + ", devicesequence=" + devicesequence + ", batchname=" + batchname
				+ ", investorid=" + investorid + ", traderid=" + traderid + ", salerid=" + salerid + ", type=" + type
				+ "]";
	}
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
	public String getBatchname() {
		return batchname;
	}
	public void setBatchname(String batchname) {
		this.batchname = batchname;
	}
	public String getInvestorid() {
		return investorid;
	}
	public void setInvestorid(String investorid) {
		this.investorid = investorid;
	}
	public String getTraderid() {
		return traderid;
	}
	public void setTraderid(String traderid) {
		this.traderid = traderid;
	}
	public String getSalerid() {
		return salerid;
	}
	public void setSalerid(String salerid) {
		this.salerid = salerid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
