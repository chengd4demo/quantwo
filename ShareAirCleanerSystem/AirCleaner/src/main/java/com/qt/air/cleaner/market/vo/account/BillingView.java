package com.qt.air.cleaner.market.vo.account;

import java.io.Serializable;

import com.qt.air.cleaner.market.domain.account.Billing;

public class BillingView implements Serializable{
	private static final long serialVersionUID = 8773796144126424859L;
	private String id;
	private String billingId;//订单ID
	private String machNo;//设备编号
	private String deviceId;//设备ID
	private String priceId;//价格ID
	private Integer costTime;//消费时长
	private Float unitPrice;//单价
	private String discountStr;//折扣率
	private Float amount;//支付金额
	private Integer state;//订单状态
	private String errorCode;//错误编码
	private String errorMsg;//错误信息
	private String transactionId;//交易流水号
	private String createTimeStr;//消费时间
	
	public BillingView () {
		super();
	}
	
	public BillingView (Billing billing) {
		this.setBillingId(billing.getBillingId());
		this.setMachNo(billing.getMachNo());
		this.setDeviceId(billing.getDeviceId());
		this.setPriceId(billing.getPriceId());
		this.setCostTime(billing.getCostTime());
		this.setUnitPrice(billing.getUnitPrice());
		this.setDiscountStr(billing.getDiscountStr());
		this.setAmount(billing.getAmount());
		this.setState(billing.getState());
		this.setErrorCode(billing.getErrorCode());
		this.setErrorMsg(billing.getErrorMsg());
		this.setTransactionId(billing.getTransactionId());
		this.setCreateTimeStr(billing.getCreateTimeStr());
		
		
		
	}

	public String getId() {
		return id;
	}

	public String getBillingId() {
		return billingId;
	}

	public String getMachNo() {
		return machNo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getPriceId() {
		return priceId;
	}

	
	public Integer getCostTime() {
		return costTime;
	}

	public Float getUnitPrice() {
		return unitPrice;
	}


	public String getDiscountStr() {
		return discountStr;
	}

	public Integer getState() {
		return state;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}

	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setDiscountStr(String discountStr) {
		this.discountStr = discountStr;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "BillingView [id=" + id + ", billingId=" + billingId + ", machNo=" + machNo + ", deviceId=" + deviceId
				+ ", priceId=" + priceId + ", costTime=" + costTime + ", unitPrice=" + unitPrice + ", discountStr="
				+ discountStr + ", amount=" + amount + ", state=" + state + ", errorCode=" + errorCode + ", errorMsg="
				+ errorMsg + ", transactionId=" + transactionId + ", createTimeStr=" + createTimeStr + "]";
	}
	
	

	
}
