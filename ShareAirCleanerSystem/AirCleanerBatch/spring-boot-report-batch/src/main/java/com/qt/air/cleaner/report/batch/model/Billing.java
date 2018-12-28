
package com.qt.air.cleaner.report.batch.model;

import java.util.Date;

public class Billing {
	private String machNo;
	private Float amount;
	private Integer count;
	private Date createTime;
	private String traderName;
	private String investorName;
	public String getMachNo() {
		return machNo;
	}
	public Float getAmount() {
		return amount;
	}
	public Integer getCount() {
		return count;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public String getTraderName() {
		return traderName;
	}
	public String getInvestorName() {
		return investorName;
	}
	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	@Override
	public String toString() {
		return "Billing [machNo=" + machNo + ", amount=" + amount + ", count=" + count + ", createTime=" + createTime
				+ ", traderName=" + traderName + ", investorName=" + investorName + "]";
	}
	
	
	
}
