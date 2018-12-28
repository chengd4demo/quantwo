package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;

public class DeviceBatchView implements Serializable {
	private static final long serialVersionUID = 8773796144126424859L;
	private String id;
	private String batchName;
	private String remarks;
	private String batchNo;
	private String investorId;
	private String investorName;
	private String createTime;
	private String legend;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public DeviceBatchView() {
		super();
	}
	public DeviceBatchView(DeviceBatch deviceBatch){
		super();
		this.setId(deviceBatch.getId());
		this.setBatchName(deviceBatch.getBatchName());
		this.setRemarks(deviceBatch.getRemarks());
		this.setBatchNo(deviceBatch.getBatchNo());
		this.setCreateTime(dateFormat.format(deviceBatch.getCreateTime()));
		if (deviceBatch.getInvestor() != null) {
			this.setInvestorId(deviceBatch.getInvestor().getId());
			this.setInvestorName(deviceBatch.getInvestor().getName());
		}
	}
	public String getId() {
		return id;
	}
	public String getBatchName() {
		return batchName;
	}
	public String getRemarks() {
		return remarks;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public String getInvestorId() {
		return investorId;
	}
	public String getInvestorName() {
		return investorName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public String toString() {
		return "DeviceBatchView [id=" + id + ", batchName=" + batchName + ", remarks=" + remarks + ", batchNo="
				+ batchNo + ", investorId=" + investorId + ", investorName=" + investorName + ", createTime="
				+ createTime + ", legend=" + legend + ", dateFormat=" + dateFormat + "]";
	}
}
