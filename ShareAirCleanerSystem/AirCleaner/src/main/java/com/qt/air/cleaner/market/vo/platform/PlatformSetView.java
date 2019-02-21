package com.qt.air.cleaner.market.vo.platform;

import java.io.Serializable;

public class PlatformSetView implements Serializable {
	private static final long serialVersionUID = -5312179511318726154L;
	private String type;
	private String name;
	private String pid;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Override
	public String toString() {
		return "PlatformSetView [type=" + type + ", name=" + name + ", pid=" + pid + "]";
	}
}
