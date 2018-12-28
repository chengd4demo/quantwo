package com.qt.air.cleaner.market.vo.price;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.price.PriceSystem;

public class PriceSystemView implements Serializable{
	private static final long serialVersionUID = -1017617840660261248L;
	private String id;
	private String name; //价格名称
	private String description; //描述
	private Integer state; //状态 
	private String activeModelId;
	private String activeModelName;
	private String priceModelName; //价格模型名称
	private String priceModelId; //价格模型Id
	private String createTime; //添加时间
	private String creater; //添加人
	private String legend;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public PriceSystemView() {
		super();
	}
	public PriceSystemView(PriceSystem priceSystem) {
		super();
		this.setId(priceSystem.getId());
		this.setName(priceSystem.getName());
		this.setDescription(priceSystem.getDescription());
		this.setPriceModelName(priceSystem.getPriceModelName());
		this.setCreateTime(dateFormat.format(priceSystem.getCreateTime()));
		this.setCreater(priceSystem.getCreater());
		this.setState(priceSystem.getState());	
		if(priceSystem.getActiveModel() != null){
			this.setActiveModelId(priceSystem.getActiveModel().getId());
			this.setActiveModelName(priceSystem.getActiveModel().getName());
		}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActiveModelId() {
		return activeModelId;
	}
	public void setActiveModelId(String activeModelId) {
		this.activeModelId = activeModelId;
	}
	public String getActiveModelName() {
		return activeModelName;
	}
	public void setActiveModelName(String activeModelName) {
		this.activeModelName = activeModelName;
	}
	public String getPriceModelName() {
		return priceModelName;
	}
	public void setPriceModelName(String priceModelName) {
		this.priceModelName = priceModelName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getPriceModelId() {
		return priceModelId;
	}
	public void setPriceModelId(String priceModelId) {
		this.priceModelId = priceModelId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "PriceSystemView [id=" + id + ", name=" + name + ", description=" + description + ", state=" + state
				+ ", activeModelId=" + activeModelId + ", activeModelName=" + activeModelName + ", priceModelName="
				+ priceModelName + ", priceModelId=" + priceModelId + ", createTime=" + createTime + ", creater="
				+ creater + ", legend=" + legend + ", dateFormat=" + dateFormat + "]";
	}
	
	
}
