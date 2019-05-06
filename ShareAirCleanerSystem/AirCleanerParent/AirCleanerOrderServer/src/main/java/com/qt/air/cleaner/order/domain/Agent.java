package com.qt.air.cleaner.order.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "MK_AGENT")
public class Agent extends GenericEntity {
	
	private static final long serialVersionUID = -8793195501659128307L;
	
	@Column(name = "ADDRESS", nullable = true, length = 255)
	private String address;
	
	@Column(name = "ALIPAY", nullable = true, length = 40)
	private String alipay;
	
	@Column(name = "EMAIL", nullable = true, length = 40)
	private String email;
	
	@Column(name = "IDENTIFICATION_NUMBER", nullable = false, length = 40, unique = true, insertable = true,
	        updatable = true)
	private String identificationNumber;
	
	@Column(name = "JOIN_TIME", nullable = true)
	private String joinTime;
	
	@Column(name = "LEGAL_PERSON", nullable = true, length = 40)
	private String legalPerson;
	
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;
	
	@Column(name = "PHONE_NUMBER", nullable = true, length = 11)
	private String phoneNumber;
	
	@Column(name = "WEIXIN", nullable = false, length = 40)
	private String weixin;
	
	@Column(name = "TYPE", nullable = false,length = 10)
	private String type;
	
	@OneToOne(optional = false)
	private Account account;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	
	
	
}
