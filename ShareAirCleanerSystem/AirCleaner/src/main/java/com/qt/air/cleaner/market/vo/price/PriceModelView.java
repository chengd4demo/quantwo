package com.qt.air.cleaner.market.vo.price;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.price.PriceModel;

public class PriceModelView implements Serializable {
	private static final long serialVersionUID = 8773796144126424859L;
	private String id;
	private String name;
	private String description;
	private String createTime;
	private String prices;
	private String legend;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public PriceModelView () {
		super();
	}
	public PriceModelView(PriceModel priceModel) {
		super();
		this.setId(priceModel.getId());
		this.setName(priceModel.getName());
		this.setDescription(priceModel.getDescription());
		this.setCreateTime(dateFormat.format(priceModel.getCreateTime()));
		this.setPrices(priceModel.getValue());
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPrices() {
		return prices;
	}
	public void setPrices(String prices) {
		this.prices = prices;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public String toString() {
		return "PriceModelView [id=" + id + ", name=" + name + ", description=" + description + ", createTime="
				+ createTime + ", prices=" + prices + ", legend=" + legend + "]";
	}
	
	
}
