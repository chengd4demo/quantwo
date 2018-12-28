package com.qt.air.cleaner.market.domain.account;

import java.text.SimpleDateFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qt.air.cleaner.market.domain.generic.Trader;
import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "ACT_WINING_CONFIG")
public class WinningConfing extends GenericEntity {
	private static final long serialVersionUID = 7327402634035983480L;
	private String id;
	@Transient
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//奖项名称
	@Column(name="AWARDS_NAME")
	private String awardsName;
	//奖品名称
	@Column(name="PRIZE_NAME")
	private String prizeName;
	//手机号码
	@Column(name="PHONE")
	private String phone;
	//姓名
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	//地址
	@Column(name="ADDRESS")
	private String address;
	//是否兑奖
	@Column(name="STATE")
	private Integer state;
	//中奖时间
	@Column(name="WINNING_TIME")
	private String winningTime;
	//兑奖时间
	@Column(name="PRIZE_TIME")
	private String prizeTime;
	//所属商家
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Trader trader;	
	//物流号
	@Column(name="LOGISTICS_NUM")
	private String logisticsNum;
	//邮编
	@Column(name="POST_CODE")
	private String postCode;
	
	
	public String getId() {
		return id;
	}
	public String getAwardsName() {
		return awardsName;
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
	public Trader getTrader() {
		return trader;
	}
	public String getLogisticsNum() {
		return logisticsNum;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAwardsName(String awardsName) {
		this.awardsName = awardsName;
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
	public void setTrader(Trader trader) {
		this.trader = trader;
	}
	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}
	
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	@Override
	public String toString() {
		return "WinningConfing [id=" + id + ", awardsName=" + awardsName + ", prizeName=" + prizeName + ", phone="
				+ phone + ", customerName=" + customerName + ", address=" + address + ", state=" + state
				+ ", winningTime=" + winningTime + ", prizeTime=" + prizeTime + ", trader=" + trader + ", logisticsNum="
				+ logisticsNum + ", postCode=" + postCode + "]";
	}
	
	
}
