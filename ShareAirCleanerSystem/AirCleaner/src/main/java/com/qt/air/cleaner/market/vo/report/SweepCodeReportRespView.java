package com.qt.air.cleaner.market.vo.report;

import java.io.Serializable;

import net.sf.json.JSONArray;

public class SweepCodeReportRespView implements Serializable {
	private static final long serialVersionUID = -8622224245616612442L;
	private String[] devices;
	private String[] dates;
	private JSONArray series;
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
	
	
}
