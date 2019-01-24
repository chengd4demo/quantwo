package com.qt.air.cleaner.web.customer.vo;

import java.io.Serializable;


public class CompleteBilling implements Serializable{

	private static final long serialVersionUID = 6123617110841838749L;
	private String code;
	private String priceId;
	private String ipAddress;
	private Object device;
	private Object priceValue;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Object getDevice() {
		return device;
	}
	public void setDevice(Object device) {
		this.device = device;
	}
	public Object getPriceValue() {
		return priceValue;
	}
	public void setPriceValue(Object priceValue) {
		this.priceValue = priceValue;
	}
	public CompleteBilling(String code, String priceId, String ipAddress, Object device, Object priceValue) {
		this.code = code;
		this.priceId = priceId;
		this.ipAddress = ipAddress;
		this.device = device;
		this.priceValue = priceValue;
	}
	public CompleteBilling() {
	}
	
}
