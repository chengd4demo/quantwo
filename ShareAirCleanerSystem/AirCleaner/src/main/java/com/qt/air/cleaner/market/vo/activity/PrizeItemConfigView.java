package com.qt.air.cleaner.market.vo.activity;

import java.io.Serializable;

import com.qt.air.cleaner.market.domain.activity.PrizeItemConfig;

public class PrizeItemConfigView implements Serializable{
	private static final long serialVersionUID = 2299335263634422262L;
	private String id;
	private String name; //奖项名称
	private String picturePath; //奖项图片
	private String remarks; //备注
	private String probabilityLable; //中奖几率
	private String legend;
	public PrizeItemConfigView() {
		super();
	}
	public PrizeItemConfigView(PrizeItemConfig prizeItemConfig) {
		this.setId(prizeItemConfig.getId());
		this.setName(prizeItemConfig.getName());
		this.setPicturePath(prizeItemConfig.getPicturePath());
		this.setRemarks(prizeItemConfig.getRemarks());
		this.setProbabilityLable(prizeItemConfig.getProbabilityLable());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getProbabilityLable() {
		return probabilityLable;
	}
	public void setProbabilityLable(String probabilityLable) {
		this.probabilityLable = probabilityLable;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public String toString() {
		return "PrizeItemConfigView [id=" + id + ", name=" + name + ", picturePath=" + picturePath + ", remarks="
				+ remarks + ", probabilityLable=" + probabilityLable + ", legend=" + legend + "]";
	}
	
}
