package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;

public class DeviceRateView implements Serializable{
	private static final long serialVersionUID = 3136001109933219767L;
	
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
	public String getInvestorlegalperson() {
		return investorlegalperson;
	}
	public void setInvestorlegalperson(String investorlegalperson) {
		this.investorlegalperson = investorlegalperson;
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
	public String getLastusetime() {
		return lastusetime;
	}
	public void setLastusetime(String lastusetime) {
		this.lastusetime = lastusetime;
	}
	private String machno; //设备编号
	private String devicesequence; //序列号
	private String batchname; //设备批次名
	private String setuptime; //安装时间
	private String setupaddress; //安装地址
	private String investorlegalperson; //投资商名		
	private String tradername; //商家名	
	private String salername; //促销员名
	private String lastusetime; //最后使用时间
	
	
	
	
}
