package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.Trader;

public class TraderView implements Serializable{
	private static final long serialVersionUID = -7717176229285141557L;	
	private String id;
	private String socialCreditCode;
	private String name;
	private String address;
	private String legalPerson;
	private String phoneNumber;
	private String weixin;
	private String email;
	private String joinTime;
	private String legend;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public TraderView() {
		super();		
	}
	public TraderView(Trader trader) {
			super();
			this.setId(trader.getId());
			this.setSocialCreditCode(trader.getSocialCreditCode());
			this.setName(trader.getName());
			this.setAddress(trader.getAddress());
			this.setLegalPerson(trader.getLegalPerson());
			this.setPhoneNumber(trader.getPhoneNumber());
			this.setWeixin(trader.getWeixin());
			this.setEmail(trader.getEmail());
			this.setJoinTime(trader.getJoinTime());			
		}	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSocialCreditCode() {
		return socialCreditCode;
	}
	public void setSocialCreditCode(String socialCreditCode) {
		this.socialCreditCode = socialCreditCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getJoinTime() {
		return joinTime;
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
		return "TraderView [id=" + id + ", socialCreditCode=" + socialCreditCode + ", name=" + name + ", address="
				+ address + ", legalPerson=" + legalPerson + ", phoneNumber=" + phoneNumber + ", weixin=" + weixin
				+ ", email=" + email + ", joinTime=" + joinTime + ", legend=" + legend + ", dateFormat=" + dateFormat
				+ "]";
	}
	
}
