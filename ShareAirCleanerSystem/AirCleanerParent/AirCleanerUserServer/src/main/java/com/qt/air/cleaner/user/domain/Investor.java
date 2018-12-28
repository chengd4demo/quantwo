
package com.qt.air.cleaner.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "MK_INVESTOR")
public class Investor extends GenericEntity {
	
	private static final long serialVersionUID = -2104708059490203341L;
	
	@Column(name = "IDENTIFICATION_NUMBER", nullable = false, length = 40, unique = true, insertable = true,
	        updatable = true)
	private String identificationNumber;
	
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;
	
	@Column(name = "ADDRESS", nullable = true, length = 255)
	private String address;
	
	@Column(name = "LEGAL_PERSON", nullable = true, length = 40)
	private String legalPerson;
	
	@Column(name = "PHONE_NUMBER", nullable = true, length = 11)
	private String phoneNumber;
	
	@Column(name = "EMAIL", nullable = true, length = 40)
	private String email;
	
	@Column(name = "JOIN_TIME", nullable = true)
	private String joinTime;
	
	@Column(name = "WEIXIN", nullable = false, length = 40)
	private String weixin;
	
	@Column(name = "ALIPAY", nullable = true, length = 40)
	private String alipay;
	
	@OneToOne(optional = false)
	private Account account;
		
	public String getIdentificationNumber() {
		
		return identificationNumber;
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
	
	public String getEmail() {
		
		return email;
	}
	
	public void setEmail(String email) {
		
		this.email = email;
	}
	
	public String getJoinTime() {
		
		return joinTime;
	}
	
	public void setJoinTime(String joinTime) {
		
		this.joinTime = joinTime;
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

	
	public Account getAccount() {
		
		return account;
	}

	
	public void setAccount(Account account) {
		
		this.account = account;
	}
	
	
}
