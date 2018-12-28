package com.qt.air.cleaner.device.vo;

import java.io.Serializable;

public class Price implements Serializable {
	private static final long serialVersionUID = -1287096820440031943L;
	private String priceId;

	private Integer costTime;

	private Float unitPrice = 0f;

	private Float discount = 0f;

	private Float realPrice = 0f;

	public String getPriceId() {

		return priceId;
	}

	public void setPriceId(String priceId) {

		this.priceId = priceId;
	}

	public Integer getCostTime() {

		return costTime;
	}

	public void setCostTime(Integer costTime) {

		this.costTime = costTime;
	}

	public Float getUnitPrice() {

		return unitPrice;
	}

	public void setUnitPrice(Float unitPrice) {

		this.unitPrice = unitPrice;
	}

	public Float getDiscount() {

		return discount;
	}

	public void setDiscount(Float discount) {

		this.discount = discount;
	}

	public Float getRealPrice() {

		return realPrice;
	}

	public void setRealPrice(Float realPrice) {

		this.realPrice = realPrice;
	}
}
