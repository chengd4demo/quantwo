package com.qt.air.cleaner.market.domain.activity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "ACT_PRIZE_ITEM_CONFIG")
public class PrizeItemConfig extends GenericEntity {
	private static final long serialVersionUID = 7327402634035983480L;
	private String id;
	private String name;
	private String picturePath;
	private Float probability = 0.00f;
	@Transient
	private String probabilityLable;
	private String remarks;
	
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
	public Float getProbability() {
		return probability;
	}
	public void setProbability(Float probability) {
		this.probability = probability;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getProbabilityLable() {
		BigDecimal bigDeciaml = new BigDecimal(this.probability * 100);
		bigDeciaml.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		return bigDeciaml.toString()+"%";
	}
	
	public void setProbabilityLable(String probabilityLable) {
		BigDecimal bigDeciaml = new BigDecimal(Float.parseFloat(probabilityLable) / 100);
		this.probability = bigDeciaml.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
}
