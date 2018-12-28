package com.qt.air.cleaner.market.domain.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qt.air.cleaner.market.domain.generic.Trader;
import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "ACT_PRIZE_CONFIG")
public class PrizeConfig extends GenericEntity{
	private static final long serialVersionUID = -9173325142897439346L;
	
	@Transient
	// 奖品品类业务处理虚拟奖
	public static final Integer PRIZE_CONFIG_VIRTUAL = 0;
	
	@Transient
	// 奖品品类业务处理虚拟奖
	public static final Integer PRIZE_CONFIG_MATERIAL = 1;
	
	@Transient
	// 奖品品类业务处理兑换奖
	public static final Integer PRIZE_CONFIG_EXCHANGE = 2;
	
	@Transient
	// 奖品品类业务处理打折卷
	public static final Integer PRIZE_CONFIG_DISCOUNT = 3;
	
	@Transient
	// 奖品品类业务处理抵扣卷
	public static final Integer PRIZE_CONFIG_DEDUCTIBLE = 4;
	
	@Transient
	// 奖品品类业务处理满减卷
	public static final Integer PRIZE_CONFIG_SUBTRACTION = 5;
	
	@Transient
	// 奖品品类业务处理状态
	public static final Integer PRIZE_CONFIG_REFUND = 6;
	
	@Transient
	// 奖品品类业务处理状态
	public static final Integer PRIZE_CONFIG_SUCCESS = 7;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	private PrizeItemConfig prizeItemConfig;
		
	@ManyToOne(fetch = FetchType.LAZY)
	private Trader trader;
	
	@Column(name = "PRIZE_NAME",length = 40)
	private String prizeName;
	
	@Column(name = "PRIZE_NUMBER",length = 255)
	private String prizeNumber;
	
	@Column(name = "PRIZE_CATEGORY",length = 40)
	private String prizeCategory;
	
	@Column(name = "STATE",length = 40)
	private String state;
	
	@Column(name = "PRIZETYPE",length = 40)
	private String prizeType;
	
	@Column(name = "DENOMINATION",length = 126)
	private Float denomination;
	
	@Column(name = "EFFECTIVE_TIME",length = 40)
	private String effectiveTime;
	
	@Column(name = "ADDRESS",length = 255)
	private String address;
	
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
	public PrizeItemConfig getPrizeItemConfig() {
		return prizeItemConfig;
	}
	public void setPrizeItemConfig(PrizeItemConfig prizeItemConfig) {
		this.prizeItemConfig = prizeItemConfig;
	}
	public Trader getTrader() {
		return trader;
	}
	public void setTrader(Trader trader) {
		this.trader = trader;
	}

}
