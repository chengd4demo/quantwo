package com.qt.air.cleaner.market.vo.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.activity.Customer;

public class CustomerView implements Serializable{
	private static final long serialVersionUID = 6621896876203073487L;	
	private String id;
	private String nickName;
	private String phoneNumber;
	private String name;
	private Integer sex;
	private String joinTime;
	private String address;
	private String identificationNumber;
	
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public CustomerView() {
		super();		
	}
	
	public CustomerView(Customer customer) {
		super();
		this.setId(customer.getId());
		this.setNickName(customer.getNickName());
		this.setPhoneNumber(customer.getPhoneNumber());
		this.setName(customer.getName());
		this.setSex(customer.getSex());
		this.setJoinTime(customer.getJoinTime());
		this.setAddress(customer.getAddress());
		this.setIdentificationNumber(customer.getIdentificationNumber());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String toString() {
		return "CustomerView [id=" + id + ", nickName=" + nickName + ", phoneNumber=" + phoneNumber + ", name=" + name
				+ ", sex=" + sex + ", joinTime=" + joinTime + ", address=" + address + ", identificationNumber="
				+ identificationNumber + ", dateFormat=" + dateFormat + "]";
	}
	
	
}
