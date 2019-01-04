package com.qt.air.cleaner.user.vo;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = -8232655676635510189L;
	private String id;
	private String identificationNumber;
	private String name;
	private Integer sex;
	private String headerUrl;
	private String nickName;
	private String weixin;
	private String userType;
	private String phoneNumber;
	private boolean isAlipay = false;
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getHeaderUrl() {
		return headerUrl;
	}
	public void setHeaderUrl(String headerUrl) {
		this.headerUrl = headerUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public boolean isAlipay() {
		return isAlipay;
	}
	public void setAlipay(boolean isAlipay) {
		this.isAlipay = isAlipay;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", identificationNumber=" + identificationNumber + ", name=" + name + ", sex="
				+ sex + ", headerUrl=" + headerUrl + ", nickName=" + nickName + ", weixin=" + weixin + ", userType="
				+ userType + ", phoneNumber=" + phoneNumber + ", isAlipay=" + isAlipay + "]";
	}
	
}
