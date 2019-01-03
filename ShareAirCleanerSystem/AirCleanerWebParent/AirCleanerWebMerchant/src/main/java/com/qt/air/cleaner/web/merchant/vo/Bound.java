package com.qt.air.cleaner.web.merchant.vo;

import java.io.Serializable;

public class Bound implements Serializable{
	private static final long serialVersionUID = -3617413495262024932L;
	
	private String openId;
	private String userType;
	private String smsCode;
	private String uniqueIdentifier;
	private String phoneNumber;
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getUserType() {
		return userType;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return "Bound [openId=" + openId + ", userType=" + userType + ", smsCode=" + smsCode + ", uniqueIdentifier="
				+ uniqueIdentifier + ", phoneNumber=" + phoneNumber + "]";
	}
	
	

	
	
	
}
