package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.Device;
public class DeviceView implements Serializable{
	private static final long serialVersionUID = 7362961235002185836L;
	private String id;
	private String machNo;
	private String lot;
	private String setupTime;
	private String setupAddress;
	private String investorName;
	private String investorId;
	private String salerName;
	private String salerId;
	private String traderName;
	private String traderId;
	private String deviceBatchId;
	private String deviceBatchName;
	private String companyId;
	private String companyName;
	private String createTime;
	private String priceSystemId;
	private String priceSystemName;
	private String legend;
	private String deviceSequence;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DeviceView() {
		super();		
	}
	public DeviceView(Device device){
		super();
		this.setId(device.getId());
		this.setMachNo(device.getMachNo());
		this.setLot(device.getLot());
		this.setSetupTime(device.getSetupTime());
		this.setSetupAddress(device.getSetupAddress());
		this.setDeviceSequence(device.getDeviceSequence());
		this.setCreateTime(dateFormat.format(device.getCreateTime()));
		if (device.getInvestor() != null) {
			this.setInvestorId(device.getInvestor().getId());
			this.setInvestorName(device.getInvestor().getName());
		}
		if(device.getSaler() != null){
			this.setSalerName(device.getSaler().getName());
			this.setSalerId(device.getSaler().getId());

		}
		if(device.getTrader() != null){
			this.setTraderId(device.getTrader().getId());
			this.setTraderName(device.getTrader().getName());
		}
		if(device.getDeviceBatch() != null){
			this.setDeviceBatchId(device.getDeviceBatch().getId());
			this.setDeviceBatchName(device.getDeviceBatch().getBatchName());
		}
		if(device.getCompany() != null){
			this.setCompanyId(device.getCompany().getId());
			this.setCompanyName(device.getCompany().getName());
		}
		if(device.getPriceSystem() != null){
			this.setPriceSystemId(device.getPriceSystem().getId());
			this.setPriceSystemName(device.getPriceSystem().getName());
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
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
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
	public String getInvestorName() {
		return investorName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public String getTraderName() {
		return traderName;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public String getTraderId() {
		return traderId;
	}
	public void setTraderId(String traderId) {
		this.traderId = traderId;
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
	public String getDeviceSequence() {
		return deviceSequence;
	}
	public void setDeviceSequence(String deviceSequence) {
		this.deviceSequence = deviceSequence;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPriceSystemId() {
		return priceSystemId;
	}
	public void setPriceSystemId(String priceSystemId) {
		this.priceSystemId = priceSystemId;
	}
	public String getPriceSystemName() {
		return priceSystemName;
	}
	public void setPriceSystemName(String priceSystemName) {
		this.priceSystemName = priceSystemName;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "DeviceView [id=" + id + ", machNo=" + machNo + ", lot=" + lot + ", setupTime=" + setupTime
				+ ", setupAddress=" + setupAddress + ", investorName=" + investorName + ", investorId=" + investorId
				+ ", salerName=" + salerName + ", salerId=" + salerId + ", traderName=" + traderName + ", traderId="
				+ traderId + ", deviceBatchId=" + deviceBatchId + ", deviceBatchName=" + deviceBatchName
				+ ", companyId=" + companyId + ", companyName=" + companyName + ", createTime=" + createTime
				+ ", priceSystemId=" + priceSystemId + ", priceSystemName=" + priceSystemName + ", legend=" + legend
				+ ", deviceSequence=" + deviceSequence + ", dateFormat=" + dateFormat + "]";
	}
}
