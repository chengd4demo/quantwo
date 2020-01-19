package com.qt.air.cleaner.market.domain.generic;

import java.io.Serializable;

public class DeviceChip implements Serializable {

	private static final long serialVersionUID = -5430004741484021620L;
	private String machno; //设备编号
	private String devicesequence; //序列号
	private String batchname; //设备批次名
	private String setuptime; //安装时间
	private String setupaddress; //安装地址
	private String legalperson; //投资商名
	private String tradername; //商家名	
	private String salername; //促销员名
	private Integer renascencetime; //滤芯总生命时长
	private Integer employtime; //已使用时长
	private Integer surplustime; //剩余时长
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
	public String getSetuptime() {
		return setuptime;
	}
	public void setSetuptime(String setuptime) {
		this.setuptime = setuptime;
	}
	public String getSetupaddress() {
		return setupaddress;
	}
	public void setSetupaddress(String setupaddress) {
		this.setupaddress = setupaddress;
	}
	public String getLegalperson() {
		return legalperson;
	}
	public void setLegalperson(String legalperson) {
		this.legalperson = legalperson;
	}
	public String getTradername() {
		return tradername;
	}
	public void setTradername(String tradername) {
		this.tradername = tradername;
	}
	public String getSalername() {
		return salername;
	}
	public void setSalername(String salername) {
		this.salername = salername;
	}
	public Integer getRenascencetime() {
		return renascencetime;
	}
	public void setRenascencetime(Integer renascencetime) {
		this.renascencetime = renascencetime;
	}
	public Integer getEmploytime() {
		return employtime;
	}
	public void setEmploytime(Integer employtime) {
		this.employtime = employtime;
	}
	public Integer getSurplustime() {
		return surplustime;
	}
	public void setSurplustime(Integer surplustime) {
		this.surplustime = surplustime;
	}
	
}
