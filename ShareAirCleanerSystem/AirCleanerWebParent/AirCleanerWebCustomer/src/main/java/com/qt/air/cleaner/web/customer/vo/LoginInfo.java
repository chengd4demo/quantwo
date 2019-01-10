package com.qt.air.cleaner.web.customer.vo;

import java.io.Serializable;

public class LoginInfo implements Serializable{
	private static final long serialVersionUID = 8206360052415163138L;
	
	private String nickName;
	private String headerUrl;
	private String weixin;
	private String verificationCode;
	private String phoneNumber;
	private String inVerificationCode;
	private int sex = 1;
	private String address;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeaderUrl() {
		return headerUrl;
	}
	public void setHeaderUrl(String headerUrl) {
		this.headerUrl = headerUrl;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getInVerificationCode() {
		return inVerificationCode;
	}
	public void setInVerificationCode(String inVerificationCode) {
		this.inVerificationCode = inVerificationCode;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "LoginInfo [nickName=" + nickName + ", headerUrl=" + headerUrl + ", weixin=" + weixin
				+ ", verificationCode=" + verificationCode + ", phoneNumber=" + phoneNumber + ", inVerificationCode="
				+ inVerificationCode + ", sex=" + sex + ", address=" + address + "]";
	}
	
	
}
