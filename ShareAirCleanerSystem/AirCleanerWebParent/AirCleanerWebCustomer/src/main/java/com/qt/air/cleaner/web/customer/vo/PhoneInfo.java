package com.qt.air.cleaner.web.customer.vo;

import java.io.Serializable;

public class PhoneInfo implements Serializable{
	private static final long serialVersionUID = 4066844473896463358L;
	private String phoneNumber;
	private String openId;
	private String userType;
	private String verificationCode;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	@Override
	public String toString() {
		return "UpdatePhoneNumber [phoneNumber=" + phoneNumber + ", openId=" + openId + ", userType=" + userType
				+ ", verificationCode=" + verificationCode + "]";
	}
			
}
