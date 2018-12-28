package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;

import com.qt.air.cleaner.market.domain.generic.Investor;

public class InvestorView implements Serializable {
	private static final long serialVersionUID = -129259180856065341L;
	private String id;
	private String name; //投资商名称
	private String legalPerson;	//负责人
	private String address;	//投资商地址
	private String identificationNumber; //身份唯一识别号
	private String phoneNumber; //电话号码
	private String email;	//邮箱
	private String weixin;	//微信号
	private String joinTime;	//加入时间
	private String legend;
	
	public InvestorView() {
		super();
	}
	public InvestorView(Investor investor) {
		this.setId(investor.getId());
		this.setName(investor.getName());
		this.setLegalPerson(investor.getLegalPerson());
		this.setAddress(investor.getAddress());
		this.setIdentificationNumber(investor.getIdentificationNumber());
		this.setPhoneNumber(investor.getPhoneNumber());
		this.setEmail(investor.getEmail());
		this.setWeixin(investor.getWeixin());
		this.setJoinTime(investor.getJoinTime());
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public String getAddress() {
		return address;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public String getWeixin() {
		return weixin;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public String toString() {
		return "InvestorView [id=" + id + ", name=" + name + ", legalPerson=" + legalPerson + ", address=" + address
				+ ", identificationNumber=" + identificationNumber + ", phoneNumber=" + phoneNumber + ", email=" + email
				+ ", weixin=" + weixin + ", joinTime=" + joinTime + "]";
	}
}
