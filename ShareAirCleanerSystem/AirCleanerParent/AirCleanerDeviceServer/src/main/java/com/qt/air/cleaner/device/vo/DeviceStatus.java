package com.qt.air.cleaner.device.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceStatus implements Serializable {
	private static final long serialVersionUID = -484787224092966026L;
	private String deviceId;

	private Integer onLine = 0;

	private Integer turnOn = 0;

	private Integer pm25 = 0;

	private Boolean available = false;

	private String message;

	private List<Price> price = new ArrayList<Price>();

	public String getDeviceId() {

		return deviceId;
	}

	public void setDeviceId(String deviceId) {

		this.deviceId = deviceId;
	}

	public Integer getOnLine() {

		return onLine;
	}

	public void setOnLine(Integer onLine) {

		this.onLine = onLine;
	}

	public Integer getTurnOn() {

		return turnOn;
	}

	public void setTurnOn(Integer turnOn) {

		this.turnOn = turnOn;
	}

	public Integer getPm25() {

		return pm25;
	}

	public void setPm25(Integer pm25) {

		this.pm25 = pm25;
	}

	public List<Price> getPrice() {

		return price;
	}

	public void setPrice(List<Price> price) {

		this.price = price;
	}

	public Boolean getAvailable() {

		return available;
	}

	public void setAvailable(Boolean available) {

		this.available = available;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}
}
