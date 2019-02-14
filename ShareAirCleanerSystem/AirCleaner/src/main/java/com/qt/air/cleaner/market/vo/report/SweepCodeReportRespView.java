package com.qt.air.cleaner.market.vo.report;

import java.io.Serializable;

import net.sf.json.JSONArray;

public class SweepCodeReportRespView implements Serializable {
	private static final long serialVersionUID = -8622224245616612442L;
	private String[] devices;
	private String[] dates;
	private JSONArray series;
	private Long total = 0L;
	private Float rate = 0.00f;
	public String[] getDevices() {
		return devices;
	}
	public void setDevices(String[] devices) {
		this.devices = devices;
	}
	public String[] getDates() {
		return dates;
	}
	public void setDates(String[] dates) {
		this.dates = dates;
	}
	public JSONArray getSeries() {
		return series;
	}
	public void setSeries(JSONArray series) {
		this.series = series;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	
}
