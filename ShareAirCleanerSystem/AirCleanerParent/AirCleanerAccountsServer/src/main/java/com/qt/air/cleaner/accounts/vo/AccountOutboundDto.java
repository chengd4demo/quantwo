package com.qt.air.cleaner.accounts.vo;

import java.io.Serializable;

public class AccountOutboundDto implements Serializable {
	private static final long serialVersionUID = 853551191094599489L;
	private String createdate;
	private Float amount = 0.00f;
	private String state;
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
