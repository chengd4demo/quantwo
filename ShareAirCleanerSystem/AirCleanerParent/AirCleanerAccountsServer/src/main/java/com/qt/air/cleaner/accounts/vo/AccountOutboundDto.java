package com.qt.air.cleaner.accounts.vo;

import java.io.Serializable;

public class AccountOutboundDto implements Serializable {
	private static final long serialVersionUID = 853551191094599489L;
	private String createdate;
	private Float amount = 0.00f;
	private String state;
	private String id;
	private boolean showbutton = false;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isShowbutton() {
		return showbutton;
	}
	public void setShowbutton(boolean showbutton) {
		this.showbutton = showbutton;
	}
	
}
