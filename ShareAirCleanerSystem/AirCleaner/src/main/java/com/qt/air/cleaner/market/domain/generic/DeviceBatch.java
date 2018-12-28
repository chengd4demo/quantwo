package com.qt.air.cleaner.market.domain.generic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "MK_DEVICE_BATCH")
public class DeviceBatch  extends GenericEntity {
	private static final long serialVersionUID = 4407148702640927396L;
	@ManyToOne(fetch = FetchType.LAZY)
	private Investor investor;
	@Column(name = "BATCH_NAME", nullable = false, length = 255)
	private String batchName;
	@Column(name = "REMARKS", nullable = false, length = 255)
	private String remarks;
	@Column(name = "BATCH_NO", nullable = false, length = 255, unique = true, insertable = true, updatable = false)
	private String batchNo;
	public Investor getInvestor() {
		return investor;
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
	public void setInvestor(Investor investor) {
		this.investor = investor;
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
	@Override
	public String toString() {
		return "DeviceBatch [investor=" + investor + ", batchName=" + batchName + ", remarks=" + remarks + ", batchNo="
				+ batchNo + "]";
	}
}
