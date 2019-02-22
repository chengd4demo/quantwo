package com.qt.air.cleaner.web.merchant.vo;

import java.io.Serializable;


public class AccountOutBound implements Serializable{
	
	private static final long serialVersionUID = -3686180255048571876L;
	
	private String password;
	
	private Float amount;
	
	private String weixin;
	
	private String userType;
	
	private String identificationNumber;
	
	private String name;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AccountOutBound [password=" + password + ", amount=" + amount + ", weixin=" + weixin + ", userType="
				+ userType + ", identificationNumber=" + identificationNumber + ", name=" + name + "]";
	}

	
}
