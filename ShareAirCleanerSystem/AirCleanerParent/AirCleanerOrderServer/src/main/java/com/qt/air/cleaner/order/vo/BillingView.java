package com.qt.air.cleaner.order.vo;

import java.io.Serializable;

import com.qt.air.cleaner.order.domain.Device;
import com.qt.air.cleaner.order.domain.PriceValue;

public class BillingView implements Serializable {
	private static final long serialVersionUID = 953512695542115815L;
	private Device device;
	private PriceValue priceValue;
	private String openId;

	public Device getDevice() {
		return device;
	}

	public PriceValue getPriceValue() {
		return priceValue;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public void setPriceValue(PriceValue priceValue) {
		this.priceValue = priceValue;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Override
	public String toString() {
		return "BillingView [device=" + device + ", priceValue=" + priceValue + ", openId=" + openId + "]";
	}


}
