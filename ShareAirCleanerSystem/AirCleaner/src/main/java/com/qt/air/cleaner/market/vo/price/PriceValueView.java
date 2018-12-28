
package com.qt.air.cleaner.market.vo.price;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.price.PriceValue;

public class PriceValueView implements Serializable {
	private static final long serialVersionUID = 8773796144126424859L;
	private String id;
	private Integer costTime;//时长
	private Float value;//单价
	private Float realValue;//实际价格
	private Integer discount;//折扣
	private String activeStartTime;//优惠开始时间
	private String activeEndTime;//优惠解释时间
	private String modelId;
	private String legend;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public PriceValueView () {
		super();
	}
	
	public PriceValueView(PriceValue priceValue) {
		super();
		this.setId(priceValue.getId());
		this.setModelId(priceValue.getPriceModelId());
		this.setCostTime(priceValue.getCostTime());
		this.setValue(priceValue.getValue());
		BigDecimal bigDecimal = new BigDecimal((priceValue.getDiscount())*100);
		this.setDiscount(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
		this.setRealValue(priceValue.getRealValue());
		if(null != priceValue.getActiveStartTime()) {
			this.setActiveStartTime(dateFormat.format(priceValue.getActiveStartTime()));
		}
		if(null != priceValue.getActiveEndTime()) {
			this.setActiveEndTime(dateFormat.format(priceValue.getActiveEndTime()));
		}
	}
	
	public String getId() {
		return id;
	}
	public Integer getCostTime() {
		return costTime;
	}
	public Float getValue() {
		return value;
	}
	public Integer getDiscount() {
		return discount;
	}
	public String getActiveStartTime() {
		return activeStartTime;
	}
	public String getActiveEndTime() {
		return activeEndTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public void setActiveStartTime(String activeStartTime) {
		this.activeStartTime = activeStartTime;
	}
	public void setActiveEndTime(String activeEndTime) {
		this.activeEndTime = activeEndTime;
	}

	public Float getRealValue() {
		return realValue;
	}

	public void setRealValue(Float realValue) {
		this.realValue = realValue;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	@Override
	public String toString() {
		return "PriceValueView [id=" + id + ", costTime=" + costTime + ", value=" + value + ", realValue=" + realValue
				+ ", discount=" + discount + ", activeStartTime=" + activeStartTime + ", activeEndTime=" + activeEndTime
				+ ", modelId=" + modelId + ", legend=" + legend + "]";
	}
	
	
	
	
	

	
	
	
	
}
