package com.qt.air.cleaner.web.merchant.vo;

import java.io.Serializable;

public class LoginInfo implements Serializable{
	private static final long serialVersionUID = 8206360052415163138L;
	
	private String name;
	private String nickName;
	private String headerUrl;
	private String weixin;
	private String identificationNumber;
	private String verificationCode;
	private String amount;
	private String phoneNumber;
	private String inVerificationCode;
	private int sex = 1;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	@Override
	public String toString() {
		return "LoginInfo [name=" + name + ", nickName=" + nickName + ", headerUrl=" + headerUrl + ", weixin=" + weixin
				+ ", identificationNumber=" + identificationNumber + ", verificationCode=" + verificationCode
				+ ", amount=" + amount + ", phoneNumber=" + phoneNumber + ", inVerificationCode=" + inVerificationCode
				+ ", sex=" + sex + "]";
	}
}
