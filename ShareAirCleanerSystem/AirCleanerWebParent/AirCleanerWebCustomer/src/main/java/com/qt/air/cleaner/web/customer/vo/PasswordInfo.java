package com.qt.air.cleaner.web.customer.vo;

import java.io.Serializable;

public class PasswordInfo implements Serializable{
	private static final long serialVersionUID = 4271642427644510699L;
	
	private String phoneNumber; 
	private String userType;
	private String traderPwd; 
	private	String openId;
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
	public String getTraderPwd() {
		return traderPwd;
	}
	public void setTraderPwd(String traderPwd) {
		this.traderPwd = traderPwd;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "PasswordInfo [phoneNumber=" + phoneNumber + ", userType=" + userType + ", traderPwd=" + traderPwd
				+ ", openId=" + openId + "]";
	}
	
	
}
