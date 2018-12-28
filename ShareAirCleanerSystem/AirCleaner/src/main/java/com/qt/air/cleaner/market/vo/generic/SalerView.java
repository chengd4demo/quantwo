package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;

import com.qt.air.cleaner.market.domain.generic.Saler;

public class SalerView implements Serializable{
	private static final long serialVersionUID = -129259180856065341L;
	private String id;
	private String identificationNumber;//身份证号码
	private String name;//姓名
	private Integer sex;//性别
	private String address;//地址
	private String phoneNumber;//联系电话
	private String email;//邮箱
	private String joinTime;//成立时间
	private String weixin;//微信号
	private String legend;
	
	public SalerView () {
		super();
	}
	
	public SalerView(Saler saler) {
		this.setId(saler.getId());
		this.setIdentificationNumber(saler.getIdentificationNumber());
		this.setName(saler.getName());
		this.setAddress(saler.getAddress());
		this.setPhoneNumber(saler.getPhoneNumber());
		this.setEmail(saler.getEmail());
		this.setJoinTime(saler.getJoinTime());
		this.setWeixin(saler.getWeixin());
		this.setSex(saler.getSex());
	}

	public String getId() {
		return id;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public String getName() {
		return name;
	}

	public Integer getSex() {
		return sex;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public String getWeixin() {
		return weixin;
	}

	public String getLegend() {
		return legend;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	@Override
	public String toString() {
		return "SalerView [id=" + id + ", identificationNumber=" + identificationNumber + ", name=" + name + ", sex="
				+ sex + ", address=" + address + ", phoneNumber=" + phoneNumber + ", email=" + email + ", joinTime="
				+ joinTime + ", weixin=" + weixin + ", legend=" + legend + "]";
	} 
	
}
