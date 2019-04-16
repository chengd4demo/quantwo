package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.Device;

public class DeviceChipView implements Serializable{
	private static final long serialVersionUID = -2345267931244086323L;
	
	private String id;
	private String machNo; //设备编号
	private String deviceSequence; //序列号
	private String deviceBatchId; //设备批次Id
	private String deviceBatchName; //设备批次名
	private String setupTime; //安装时间
	private String setupAddress; //安装地址
	private String companyId; //公司Id
	private String companyName; //公司名称
	private String investorId; //投资商Id
	private String investorName; //投资商名		
	private String traderId; //商家Id
	private String traderName; //商家名	
	private String salerId; //促销员Id
	private String salerName; //促销员名
	private String renascenceTime; //滤芯总生命时长
	private String employTime; //已使用时长
	private String surplusTime; //剩余时长
	
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DeviceChipView() {
		super();		
	}
	public DeviceChipView(Device deviceChip) {
		this.setId(deviceChip.getId());
		this.setMachNo(deviceChip.getMachNo());
		this.setDeviceSequence(deviceChip.getDeviceSequence());
		this.setSetupTime(deviceChip.getSetupTime());
		this.setSetupAddress(deviceChip.getSetupAddress());
		if(deviceChip.getCompany() != null){
			this.setCompanyId(deviceChip.getCompany().getId());
			this.setCompanyName(deviceChip.getCompany().getName());
		}
		if(deviceChip.getTrader() != null){
			this.setTraderId(deviceChip.getTrader().getId());
			this.setTraderName(deviceChip.getTrader().getName());
		}
		if (deviceChip.getInvestor() != null) {
			this.setInvestorId(deviceChip.getInvestor().getId());
			this.setInvestorName(deviceChip.getInvestor().getName());
		}
		if(deviceChip.getSaler() != null){			
			this.setSalerId(deviceChip.getSaler().getId());
			this.setSalerName(deviceChip.getSaler().getName());
		}
		if(deviceChip.getDeviceBatch() != null){
			this.setDeviceBatchId(deviceChip.getDeviceBatch().getId());
			this.setDeviceBatchName(deviceChip.getDeviceBatch().getBatchName());
		}		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMachNo() {
		return machNo;
	}
	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}
	public String getDeviceSequence() {
		return deviceSequence;
	}
	public void setDeviceSequence(String deviceSequence) {
		this.deviceSequence = deviceSequence;
	}
	public String getDeviceBatchId() {
		return deviceBatchId;
	}
	public void setDeviceBatchId(String deviceBatchId) {
		this.deviceBatchId = deviceBatchId;
	}
	public String getDeviceBatchName() {
		return deviceBatchName;
	}
	public void setDeviceBatchName(String deviceBatchName) {
		this.deviceBatchName = deviceBatchName;
	}
	public String getSetupTime() {
		return setupTime;
	}
	public void setSetupTime(String setupTime) {
		this.setupTime = setupTime;
	}
	public String getSetupAddress() {
		return setupAddress;
	}
	public void setSetupAddress(String setupAddress) {
		this.setupAddress = setupAddress;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public String getInvestorName() {
		return investorName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public String getTraderId() {
		return traderId;
	}
	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}
	public String getTraderName() {
		return traderName;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	public String getRenascenceTime() {
		return renascenceTime;
	}
	public void setRenascenceTime(String renascenceTime) {
		this.renascenceTime = renascenceTime;
	}
	public String getEmployTime() {
		return employTime;
	}
	public void setEmployTime(String employTime) {
		this.employTime = employTime;
	}
	public String getSurplusTime() {
		return surplusTime;
	}
	public void setSurplusTime(String surplusTime) {
		this.surplusTime = surplusTime;
	}
	@Override
	public String toString() {
		return "DeviceChipView [id=" + id + ", machNo=" + machNo + ", deviceSequence=" + deviceSequence
				+ ", deviceBatchId=" + deviceBatchId + ", deviceBatchName=" + deviceBatchName + ", setupTime="
				+ setupTime + ", setupAddress=" + setupAddress + ", companyId=" + companyId + ", companyName="
				+ companyName + ", investorId=" + investorId + ", investorName=" + investorName + ", traderId="
				+ traderId + ", traderName=" + traderName + ", salerId=" + salerId + ", salerName=" + salerName
				+ ", renascenceTime=" + renascenceTime + ", employTime=" + employTime + ", surplusTime=" + surplusTime
				+ "]";
	}
	
	
}
