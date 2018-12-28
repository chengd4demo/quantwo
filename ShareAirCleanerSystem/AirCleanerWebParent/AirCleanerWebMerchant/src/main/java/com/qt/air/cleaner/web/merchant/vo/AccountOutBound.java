package com.qt.air.cleaner.web.merchant.vo;

import java.io.Serializable;


public class AccountOutBound implements Serializable{
	
	private static final long serialVersionUID = -3686180255048571876L;
	
	private String createdate;
	
	private Float amount;
	
	private String state;
	
	private String weixin;

	public String getCreatedate() {
		return createdate;
	}

	public Float getAmount() {
		return amount;
	}

	public String getState() {
		return state;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	@Override
	public String toString() {
		return "AccountOutBound [createdate=" + createdate + ", amount=" + amount + ", state=" + state + ", weixin="
				+ weixin + "]";
	}

}
