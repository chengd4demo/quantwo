package com.qt.air.cleaner.market.domain.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.singalrain.framework.core.bo.GenericEntity;


@Entity
@Table(name = "MK_CUSTOMER")
public class Customer extends GenericEntity {
	
	private static final long serialVersionUID = 5253573435610989412L;
	
	@Column(name = "IDENTIFICATION_NUMBER", nullable = false, length = 40, unique = true, insertable = true,
	        updatable = true)
	private String identificationNumber;
	
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;
	
	@Column(name = "SEX", nullable = false, length = 1)
	private Integer sex;
	
	@Column(name = "ADDRESS", nullable = true, length = 255)
	private String address;
	
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
	
	@Column(name = "HEADER_URL", nullable = true, length = 255)
	private String headerUrl;
	
	@Column(name = "NICK_NAME", nullable = true, length = 255)
	private String nickName;
	
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
	
	public Integer getSex() {
		
		return sex;
	}
	
	public void setSex(Integer sex) {
		
		this.sex = sex;
	}
	
	public String getAddress() {
		
		return address;
	}
	
	public void setAddress(String address) {
		
		this.address = address;
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

	
}
