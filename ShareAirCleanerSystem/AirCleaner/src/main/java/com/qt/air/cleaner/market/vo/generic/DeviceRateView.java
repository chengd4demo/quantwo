package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.Device;

public class DeviceRateView implements Serializable{
	private static final long serialVersionUID = 3136001109933219767L;
	
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
	private String lastTime; //最后使用时间
	
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DeviceRateView() {
		super();		
	}
	
	public DeviceRateView(Device deviceRate) {
		this.setId(deviceRate.getId());
		this.setMachNo(deviceRate.getMachNo());
		this.setDeviceSequence(deviceRate.getDeviceSequence());
		this.setSetupTime(deviceRate.getSetupTime());
		this.setSetupAddress(deviceRate.getSetupAddress());
		if(deviceRate.getCompany() != null){
			this.setCompanyId(deviceRate.getCompany().getId());
			this.setCompanyName(deviceRate.getCompany().getName());
		}
		if(deviceRate.getTrader() != null){
			this.setTraderId(deviceRate.getTrader().getId());
			this.setTraderName(deviceRate.getTrader().getName());
		}
		if (deviceRate.getInvestor() != null) {
			this.setInvestorId(deviceRate.getInvestor().getId());
			this.setInvestorName(deviceRate.getInvestor().getName());
		}
		if(deviceRate.getSaler() != null){			
			this.setSalerId(deviceRate.getSaler().getId());
			this.setSalerName(deviceRate.getSaler().getName());
		}
		if(deviceRate.getDeviceBatch() != null){
			this.setDeviceBatchId(deviceRate.getDeviceBatch().getId());
			this.setDeviceBatchName(deviceRate.getDeviceBatch().getBatchName());
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

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String toString() {
		return "DeviceRateView [id=" + id + ", machNo=" + machNo + ", deviceSequence=" + deviceSequence
				+ ", deviceBatchId=" + deviceBatchId + ", deviceBatchName=" + deviceBatchName + ", setupTime="
				+ setupTime + ", setupAddress=" + setupAddress + ", companyId=" + companyId + ", companyName="
				+ companyName + ", investorId=" + investorId + ", investorName=" + investorName + ", traderId="
				+ traderId + ", traderName=" + traderName + ", salerId=" + salerId + ", salerName=" + salerName
				+ ", lastTime=" + lastTime + ", dateFormat=" + dateFormat + "]";
	}
	
}
