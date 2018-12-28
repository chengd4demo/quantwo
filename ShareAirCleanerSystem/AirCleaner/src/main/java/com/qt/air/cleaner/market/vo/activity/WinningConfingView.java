package com.qt.air.cleaner.market.vo.activity;

import java.io.Serializable;

import com.qt.air.cleaner.market.domain.account.WinningConfing;

public class WinningConfingView implements Serializable{
	private static final long serialVersionUID = 2299335263634422262L;
	private String id;
	private String awardsName;
	private String prizeName;
	private String phone;
	private String customerName;
	private String address;
	private Integer state;
	private String winningTime;
	private String prizeTime;
	private String trader;
	private String logisticsNum;
	private String postCode;
	
	public WinningConfingView() {
		super();
	}
	
	public WinningConfingView(WinningConfing winningConfing) {
		this.setId(winningConfing.getId());
		this.setAwardsName(winningConfing.getAwardsName());
		this.setPrizeName(winningConfing.getPrizeName());
		this.setPhone(winningConfing.getPhone());
		this.setCustomerName(winningConfing.getCustomerName());
		this.setAddress(winningConfing.getAddress());
		this.setState(winningConfing.getState());
		this.setWinningTime(winningConfing.getWinningTime());
		this.setPrizeTime(winningConfing.getPrizeTime());
		if(winningConfing.getTrader() != null) {
			this.setTrader(winningConfing.getTrader().getId());
			this.setTrader(winningConfing.getTrader().getName());
		}		
		this.setLogisticsNum(winningConfing.getLogisticsNum());
		this.setPostCode(winningConfing.getPostCode());		
	}


	public String getId() {
		return id;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public String getPhone() {
		return phone;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getAddress() {
		return address;
	}

	public Integer getState() {
		return state;
	}

	public String getWinningTime() {
		return winningTime;
	}

	public String getPrizeTime() {
		return prizeTime;
	}

	public String getTrader() {
		return trader;
	}

	public String getLogisticsNum() {
		return logisticsNum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setWinningTime(String winningTime) {
		this.winningTime = winningTime;
	}

	public void setPrizeTime(String prizeTime) {
		this.prizeTime = prizeTime;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}

	public String getAwardsName() {
		return awardsName;
	}

	public void setAwardsName(String awardsName) {
		this.awardsName = awardsName;
	}
	
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Override
	public String toString() {
		return "WinningConfingView [id=" + id + ", awardsName=" + awardsName + ", prizeName=" + prizeName + ", phone="
				+ phone + ", customerName=" + customerName + ", address=" + address + ", state=" + state
				+ ", winningTime=" + winningTime + ", prizeTime=" + prizeTime + ", trader=" + trader + ", logisticsNum="
				+ logisticsNum + ", postCode=" + postCode + "]";
	}

	
}
