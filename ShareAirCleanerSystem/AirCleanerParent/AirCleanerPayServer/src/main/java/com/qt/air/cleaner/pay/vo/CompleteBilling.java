package com.qt.air.cleaner.pay.vo;

import java.io.Serializable;

import com.qt.air.cleaner.pay.domain.Device;
import com.qt.air.cleaner.pay.domain.PriceValue;

public class CompleteBilling implements Serializable{

	private static final long serialVersionUID = 6123617110841838749L;
	private String code;
	private String priceId;
	private String ipAddress;
	private Device device;
	private PriceValue priceValue;
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
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public PriceValue getPriceValue() {
		return priceValue;
	}
	public void setPriceValue(PriceValue priceValue) {
		this.priceValue = priceValue;
	}
	public CompleteBilling(String code, String priceId, String ipAddress, Device device, PriceValue priceValue) {
		this.code = code;
		this.priceId = priceId;
		this.ipAddress = ipAddress;
		this.device = device;
		this.priceValue = priceValue;
	}
	public CompleteBilling() {
	}
	
	
	
}
