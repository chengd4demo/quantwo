package com.qt.air.cleaner.report.batch.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "REP_SCAN_CODE_AMOUNT")
public class ScanCodeAmount {
	@Id
	@GeneratedValue(generator = "guid")
	@GenericGenerator(name = "guid", strategy = "guid")
	private String id;
	@Column(name = "DEVICE_NAME", length = 255, nullable = false, insertable = true, updatable = false)
	private String deviceName;	//设备名称
	@Column(name = "COUNT", nullable = false, insertable = true, updatable = true)
	private Integer count;	//扫码量
	@Column(name = "TRADER_NAME",length = 255, nullable = false, insertable = true, updatable = false)
	private String traderName; //商家名称
	@Column(name = "INVESTOR_NAME",length = 255, nullable = false, insertable = true, updatable = false)
	private String investorName; //投资商
	@Column(name = "BILLING_TIME", nullable = false, insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date BillingTime;	//订单时间
	@Column(name = "CREATE_TIME", nullable = false, insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createTime;	//创建时间
	public String getId() {
		return id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public Integer getCount() {
		return count;
	}
	public String getTraderName() {
		return traderName;
	}
	public String getInvestorName() {
		return investorName;
	}
	public Date getBillingTime() {
		return BillingTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public void setBillingTime(Date billingTime) {
		BillingTime = billingTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
