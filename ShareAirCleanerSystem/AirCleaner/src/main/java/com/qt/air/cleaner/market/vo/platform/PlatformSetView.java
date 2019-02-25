package com.qt.air.cleaner.market.vo.platform;

import java.io.Serializable;

import com.qt.air.cleaner.market.domain.platform.ShareProfit;

public class PlatformSetView implements Serializable {
	private static final long serialVersionUID = -5312179511318726154L;
	private String id;
	private String type;
	private String name;
	private String pid;
	private String legend;
	private Float scale = null;
	private Float free = null;
	private boolean pidDisabled = false;
	private boolean typeDisabled = false;
	public PlatformSetView(){
	}
	public PlatformSetView(ShareProfit shareProfit) {
		this.setId(shareProfit.getId());
		this.setType(shareProfit.getType());
		this.setName(shareProfit.getName());
		this.setPid(shareProfit.getPid());
		this.setScale(shareProfit.getScale());
		this.setFree(shareProfit.getFree());
	}
	public boolean isPidDisabled() {
		return pidDisabled;
	}
	public void setPidDisabled(boolean pidDisabled) {
		this.pidDisabled = pidDisabled;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
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
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	public Float getScale() {
		return scale;
	}
	public void setScale(Float scale) {
		this.scale = scale;
	}
	public Float getFree() {
		return free;
	}
	public void setFree(Float free) {
		this.free = free;
	}
	public boolean isTypeDisabled() {
		return typeDisabled;
	}
	public void setTypeDisabled(boolean typeDisabled) {
		this.typeDisabled = typeDisabled;
	}
	@Override
	public String toString() {
		return "PlatformSetView [id=" + id + ", type=" + type + ", name=" + name + ", pid=" + pid + ", legend=" + legend
				+ ", scale=" + scale + ", free=" + free + ", pidDisabled=" + pidDisabled + "]";
	}
	
	

}
