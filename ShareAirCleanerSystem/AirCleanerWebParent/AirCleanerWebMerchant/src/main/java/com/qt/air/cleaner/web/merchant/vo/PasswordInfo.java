package com.qt.air.cleaner.web.merchant.vo;

import java.io.Serializable;

public class PasswordInfo implements Serializable{
	private static final long serialVersionUID = 4271642427644510699L;
	
	private String phoneNumber; 
	private String userType;
	private String tradePwd; 
	private	String openId;
	private String oldTradePwd;
	private String verificationCode;
	private String inVerificationCode;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getTradePwd() {
		return tradePwd;
	}
	public void setTradePwd(String tradePwd) {
		this.tradePwd = tradePwd;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getOldTradePwd() {
		return oldTradePwd;
	}
	public void setOldTradePwd(String oldTradePwd) {
		this.oldTradePwd = oldTradePwd;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getInVerificationCode() {
		return inVerificationCode;
	}
	public void setInVerificationCode(String inVerificationCode) {
		this.inVerificationCode = inVerificationCode;
	}
	
	@Override
	public String toString() {
		return "PasswordInfo [phoneNumber=" + phoneNumber + ", userType=" + userType + ", tradePwd=" + tradePwd
				+ ", openId=" + openId + ", oldTradePwd=" + oldTradePwd + ", verificationCode=" + verificationCode
				+ ", inVerificationCode=" + inVerificationCode + "]";
	}
	
	
	
}
