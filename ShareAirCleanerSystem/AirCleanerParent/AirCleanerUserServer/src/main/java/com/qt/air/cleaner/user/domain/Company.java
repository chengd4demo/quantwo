
package com.qt.air.cleaner.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "MK_COMPANY")
public class Company extends GenericEntity {
	
	private static final long serialVersionUID = -8793195501659128307L;
	
	@Column(name = "SOCIAL_CREDIT_CODE", nullable = false, length = 40, unique = true, insertable = true,
	        updatable = false)
	private String socialCreditCode;
	
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
	
	@Column(name = "SETUP_TIME", nullable = true)
	private String setupTime;
	
	@Column(name = "WEIXIN", nullable = false, length = 40)
	private String weixin;
	
	@Column(name = "ALIPAY", nullable = true, length = 40)
	private String alipay;
	
	@OneToOne(optional = false)
	private Account account;
	
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
	
	public String getEmail() {
		
		return email;
	}
	
	public void setEmail(String email) {
		
		this.email = email;
	}
	
	public String getSetupTime() {
		
		return setupTime;
	}
	
	public void setSetupTime(String setupTime) {
		
		this.setupTime = setupTime;
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
	
	@Override
	public String toString() {
		
		return "Company [socialCreditCode=" + socialCreditCode + ", name=" + name + ", address=" + address
		        + ", legalPerson=" + legalPerson + ", phoneNumber=" + phoneNumber + ", email=" + email + ", setupTime="
		        + setupTime + ", weixin=" + weixin + ", alipay=" + alipay + "]";
	}
}
