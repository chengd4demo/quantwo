package com.qt.air.cleaner.device.vo;

import java.io.Serializable;

public class TraderDevice implements Serializable {
	private static final long serialVersionUID = -4998091278531367656L;
	private Integer count;
	private String name;
	private Integer usecount;
	private String address;
	private String traderid;
	public Integer getCount() {
		return count;
	}
	public String getName() {
		return name;
	}
	public Integer getUsecount() {
		return usecount;
	}
	public String getAddress() {
		return address;
	}
	public String getTraderid() {
		return traderid;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUsecount(Integer usecount) {
		this.usecount = usecount;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setTraderid(String traderid) {
		this.traderid = traderid;
	}
	
}
