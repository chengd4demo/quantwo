package com.qt.air.cleaner.market.domain.account;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ACT_BILLING_WX_REFUND_REASON")
public class BillingRefundReason {
	
	@Id
	@GeneratedValue(generator = "guid")
	@GenericGenerator(name = "guid", strategy = "guid")
	private String id;
	@Column(name = "REJECT_REASON")
	private String rejectReason;
	@Column(name = "REJECT_TIME")
	private Date rejectTime;
	
	public String getId() {
		return id;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public Date getRejectTime() {
		return rejectTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}
	
}
