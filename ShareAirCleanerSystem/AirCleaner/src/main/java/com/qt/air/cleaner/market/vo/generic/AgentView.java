package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.Agent;

public class AgentView implements Serializable {
	private static final long serialVersionUID = 8942711772125719926L;
	
	private String id;
	private String name;
	private String identificationNumber;
	private String phoneNumber;
	private String weixin;
	private String alipay;
	private String address;
	private String legalPerson;
	private String joinTime;
	private String email;
	private String legend;
	
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public AgentView() {
		super();		
	}
	public AgentView(Agent agent) {
		this.setId(agent.getId());
		this.setName(agent.getName());
		this.setIdentificationNumber(agent.getIdentificationNumber());
		this.setPhoneNumber(agent.getPhoneNumber());
		this.setWeixin(agent.getWeixin());
		this.setAlipay(agent.getAlipay());
		this.setAddress(agent.getAddress());
		this.setLegalPerson(agent.getLegalPerson());
		this.setJoinTime(agent.getJoinTime());
		this.setEmail(agent.getEmail());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
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
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
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
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public String toString() {
		return "AgentView [id=" + id + ", name=" + name + ", identificationNumber=" + identificationNumber
				+ ", phoneNumber=" + phoneNumber + ", weixin=" + weixin + ", alipay=" + alipay + ", address=" + address
				+ ", legalPerson=" + legalPerson + ", joinTime=" + joinTime + ", email=" + email + ", dateFormat="
				+ dateFormat + "]";
	}
	
}
