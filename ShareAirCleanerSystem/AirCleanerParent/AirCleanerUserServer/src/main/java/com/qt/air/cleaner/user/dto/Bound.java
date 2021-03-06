package com.qt.air.cleaner.user.dto;

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

	public Bound(String openId, String userType, String smsCode, String uniqueIdentifier, String phoneNumber) {
		super();
		this.openId = openId;
		this.userType = userType;
		this.smsCode = smsCode;
		this.uniqueIdentifier = uniqueIdentifier;
		this.phoneNumber = phoneNumber;
	}

	
	
	
}
