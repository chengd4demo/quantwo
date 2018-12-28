package com.qt.air.cleaner.market.vo.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.activity.PrizeConfig;

public class PrizeConfigView implements Serializable{
	private static final long serialVersionUID = -6279063644755114145L;
	private String id;
	private String prizeItemConfigId; //奖项名称id
	private String prizeItemConfigName; //奖项名称name
	private String prizeName; //奖品名称
	private String prizeNumber; //奖品数量
	private String prizeCategory; //奖品品类
	private String traderId; //所属商家
	private String traderName; //所属商家
	private String state; //是否激活
	private String prizeType; //奖品类型
	private Float denomination; //面额
	private String effectiveTime; //有效期
	private String address; //使用地址
	private String legend;
	
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public PrizeConfigView() {
		super();
	}
	public PrizeConfigView(PrizeConfig prizeConfig) {
		this.setId(prizeConfig.getId());
		this.setPrizeName(prizeConfig.getPrizeName());
		this.setPrizeNumber(prizeConfig.getPrizeNumber());
		this.setPrizeCategory(prizeConfig.getPrizeCategory());
		this.setState(prizeConfig.getState());
		this.setPrizeType(prizeConfig.getPrizeType());
		this.setDenomination(prizeConfig.getDenomination());
		this.setEffectiveTime(prizeConfig.getEffectiveTime());
		this.setAddress(prizeConfig.getAddress());
		if(prizeConfig.getPrizeItemConfig() != null){
			this.setPrizeItemConfigId(prizeConfig.getPrizeItemConfig().getId());
			this.setPrizeItemConfigName(prizeConfig.getPrizeItemConfig().getName());		
		}
		if (prizeConfig.getTrader() != null) {
			this.setTraderId(prizeConfig.getTrader().getId());
			this.setTraderName(prizeConfig.getTrader().getName());
		}
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrizeItemConfigId() {
		return prizeItemConfigId;
	}
	public void setPrizeItemConfigId(String prizeItemConfigId) {
		this.prizeItemConfigId = prizeItemConfigId;
	}
	public String getPrizeItemConfigName() {
		return prizeItemConfigName;
	}
	public void setPrizeItemConfigName(String prizeItemConfigName) {
		this.prizeItemConfigName = prizeItemConfigName;
	}
	public String getPrizeName() {
		return prizeName;
	}
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	public String getPrizeNumber() {
		return prizeNumber;
	}
	public void setPrizeNumber(String prizeNumber) {
		this.prizeNumber = prizeNumber;
	}
	public String getPrizeCategory() {
		return prizeCategory;
	}
	public void setPrizeCategory(String prizeCategory) {
		this.prizeCategory = prizeCategory;
	}
	public String getTraderId() {
		return traderId;
	}
	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}
	public String getTraderName() {
		return traderName;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}
	public Float getDenomination() {
		return denomination;
	}
	public void setDenomination(Float denomination) {
		this.denomination = denomination;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	@Override
	public String toString() {
		return "PrizeConfigView [id=" + id + ", prizeItemConfigId=" + prizeItemConfigId + ", prizeItemConfigName="
				+ prizeItemConfigName + ", prizeName=" + prizeName + ", prizeNumber=" + prizeNumber + ", prizeCategory="
				+ prizeCategory + ", traderId=" + traderId + ", traderName=" + traderName + ", state=" + state
				+ ", prizeType=" + prizeType + ", denomination=" + denomination + ", effectiveTime=" + effectiveTime
				+ ", address=" + address + ", legend=" + legend + ", dateFormat=" + dateFormat + "]";
	}
		
}
