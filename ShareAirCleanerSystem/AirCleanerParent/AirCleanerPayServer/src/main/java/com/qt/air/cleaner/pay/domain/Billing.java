
package com.qt.air.cleaner.pay.domain;

import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "ACT_BILLING")
public class Billing extends GenericEntity {
	
	@Transient
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Transient
	public final static Integer BILLING_STATE_CANCEL = -2;
	
	@Transient
	public final static Integer BILLING_STATE_EXCEPTION = -1;
	
	
	@Transient
	public final static Integer BILLING_STATE_PENDING_PAYMENT = 0;
	
	
	@Transient
	public final static Integer BILLING_STATE_ALREADY_PAID = 1;
	
	
	@Transient
	public final static Integer BILLING_STATE_ACCOUNT_OPEN = 2;
	
	
	@Transient
	public final static Integer BILLING_STATE_ACCOUNT_OPENED = 3;
	
	
	@Transient
	public final static Integer BILLING_STATE_ACCOUNT_REFUND = 4;
	
	
	@Transient
	public final static Integer BILLING_STATE_ACCOUNT_COMPLETE = 5;
	
	private static final long serialVersionUID = 4544318391860135830L;
	
	@Version
	private Integer version;
	
	@Column(name = "BILLING_ID", nullable = false, length = 32, insertable = true, updatable = false)
	private String billingId;
	
	@Column(name = "MACH_NO", nullable = false, length = 40, insertable = true, updatable = false)
	private String machNo;
	
	@Column(name = "DEVICE_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String deviceId;
	
	@Column(name = "PRICE_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String priceId;
	
	@Column(name = "WEIXIN", nullable = false, length = 100, insertable = true, updatable = true)
	private String weixin;
	
	@Column(name = "COST_TIME", nullable = false, insertable = true, updatable = false)
	private Integer costTime;
	
	@Column(name = "UNIT_PRICE", nullable = false, insertable = true, updatable = false)
	private Float unitPrice;
	
	@Column(name = "DISCOUNT", nullable = false, insertable = true, updatable = false)
	private Float discount;
	
	@Transient
	private String discountStr;
	
	@Column(name = "AMOUNT", nullable = false, insertable = true, updatable = false)
	private Float amount;
	
	@Column(name = "STATE", nullable = false, insertable = true, updatable = true)
	private Integer state = BILLING_STATE_PENDING_PAYMENT;
	
	@Column(name = "ERROR_CODE", nullable = true, length = 32)
	private String errorCode;
	
	@Column(name = "ERROR_MSG", nullable = true, length = 128)
	private String errorMsg;
	
	@Column(name = "TRANSACTION_ID", nullable = true, length = 32)
	private String transactionId;
	
	@Transient
	private String createTimeStr;
	
	public String getBillingId() {
		
		return billingId;
	}
	
	public void setBillingId(String billingId) {
		
		this.billingId = billingId;
	}
	
	public String getMachNo() {
		
		return machNo;
	}
	
	public void setMachNo(String machNo) {
		
		this.machNo = machNo;
	}
	
	public String getDeviceId() {
		
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		
		this.deviceId = deviceId;
	}
	
	public String getPriceId() {
		
		return priceId;
	}
	
	public void setPriceId(String priceId) {
		
		this.priceId = priceId;
	}
	
	public String getWeixin() {
		
		return weixin;
	}
	
	public void setWeixin(String weixin) {
		
		this.weixin = weixin;
	}
	
	public Integer getCostTime() {
		
		return costTime;
	}
	
	public void setCostTime(Integer costTime) {
		
		this.costTime = costTime;
	}
	
	public Float getUnitPrice() {
		
		return unitPrice;
	}
	
	public void setUnitPrice(Float unitPrice) {
		
		this.unitPrice = unitPrice;
	}
	
	public Float getDiscount() {
		
		return discount;
	}
	
	public void setDiscount(Float discount) {
		
		this.discount = discount;
	}
	
	public String getDiscountStr() {
		
		if (this.getDiscount() != null) {
			return String.valueOf(this.getDiscount() * 100) + "%";
		} else {
			return "";
		}
	}
	
	public void setDiscountStr(String discountStr) {
		
		this.discountStr = discountStr;
	}
	
	public Float getAmount() {
		
		return amount;
	}
	
	public void setAmount(Float amount) {
		
		this.amount = amount;
	}
	
	public Integer getState() {
		
		return state;
	}
	
	public void setState(Integer state) {
		
		this.state = state;
	}
	
	public Integer getVersion() {
		
		return version;
	}
	
	public void setVersion(Integer version) {
		
		this.version = version;
	}
	
	public String getErrorCode() {
		
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg() {
		
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		
		this.errorMsg = errorMsg;
	}
	
	public String getTransactionId() {
		
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		
		this.transactionId = transactionId;
	}
	
	public String getCreateTimeStr() {
		
		return dateFormat.format(this.getCreateTime());
		
	}
	
	public void setCreateTimeStr(String createTimeStr) {
		
		this.createTimeStr = createTimeStr;
	}

	@Override
	public String toString() {
		return "Billing [dateFormat=" + dateFormat + ", version=" + version + ", billingId=" + billingId + ", machNo="
				+ machNo + ", deviceId=" + deviceId + ", priceId=" + priceId + ", weixin=" + weixin + ", costTime="
				+ costTime + ", unitPrice=" + unitPrice + ", discount=" + discount + ", discountStr=" + discountStr
				+ ", amount=" + amount + ", state=" + state + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg
				+ ", transactionId=" + transactionId + ", createTimeStr=" + createTimeStr + "]";
	}
	
	
}
