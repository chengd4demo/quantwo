package com.qt.air.cleaner.scheduled.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "REP_PAYMENT_RECORD")
public class PaymentRecordReport  extends GenericEntity {
	private static final long serialVersionUID = -2610263156266660005L;
	@Column(name = "MACH_NO", nullable = false, length = 40, unique = false, insertable = true, updatable = false)
	private String machNo;
	@Column(name = "TRADER_ID", length = 40, nullable = false, insertable = true, updatable = false)
	private String traderId;
	@Column(name = "COMPANY_ID", length = 40, nullable = false, insertable = true, updatable = false)
	private String companyId;
	@Column(name = "INVESTOR_ID", length = 40, nullable = false, insertable = true, updatable = false)
	private String investorId;
	@Column(name = "SALER_ID", length = 40, nullable = false, insertable = true, updatable = false)
	private String salerId;
	private Float amounts = 0.00f;
	@Temporal(TemporalType.TIMESTAMP)
	private Date sweepCodeTime;
	public String getMachNo() {
		return machNo;
	}
	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}
	public String getTraderId() {
		return traderId;
	}
	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public Float getAmounts() {
		return amounts;
	}
	public void setAmounts(Float amounts) {
		this.amounts = amounts;
	}
	public Date getSweepCodeTime() {
		return sweepCodeTime;
	}
	public void setSweepCodeTime(Date sweepCodeTime) {
		this.sweepCodeTime = sweepCodeTime;
	}
	
}
