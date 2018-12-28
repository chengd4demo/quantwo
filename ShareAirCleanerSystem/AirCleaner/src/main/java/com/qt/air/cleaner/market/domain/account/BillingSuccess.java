package com.qt.air.cleaner.market.domain.account;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.singalrain.framework.core.bo.GenericEntity;
import com.singalrain.framework.util.StringUtils;

@Entity
@Table(name = "ACT_BILLING_WX_SUCCESS")
public class BillingSuccess extends GenericEntity {

	private static final long serialVersionUID = 5779142004544894079L;

	@Transient
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 交易时间
	 */
	@Column(name = "TRADING_TIME", nullable = false, insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradingTime;

	/**
	 * 公众账号ID,
	 */
	@Column(name = "APP_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String appId;

	/**
	 * 商户号
	 */
	@Column(name = "MACH_NO", nullable = false, length = 40, insertable = true, updatable = false)
	private String machNo;

	/**
	 * 子商户号
	 */
	@Column(name = "SUB_MACH_NO", nullable = false, length = 40, insertable = true, updatable = false)
	private String subMachNo;

	/**
	 * 设备号
	 */
	@Column(name = "DEVICE_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String deviceId;

	/**
	 * 微信订单号
	 */
	@Column(name = "BILLING_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String billingId;

	/**
	 * 商户订单号
	 */
	@Column(name = "TRANSACTION_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String transactionId;

	/**
	 * 用户标识
	 */
	@Column(name = "OPEN_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String openId;

	/**
	 * 交易类型
	 */
	@Column(name = "TRADING_TYPE", nullable = false, length = 10, insertable = true, updatable = false)
	private String tradingType;

	/**
	 * 交易状态
	 */
	@Column(name = "TRADING_STATUS", nullable = false, length = 10, insertable = true, updatable = false)
	private String tradingStatus;
	/**
	 * 付款银行
	 */
	@Column(name = "BANK", nullable = false, length = 20, insertable = true, updatable = false)
	private String bank;

	/**
	 * 货币种类
	 */
	@Column(name = "CURRENCY", nullable = false, length = 10, insertable = true, updatable = false)
	private String currency;

	/**
	 * 总金额
	 */
	@Column(name = "TOTAL_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 2)
	private Float totalFee = 0f;

	/**
	 * 代金券或立减优惠金额
	 */
	@Column(name = "DISCOUNT_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 2)
	private Float discountFee = 0f;

	/**
	 * 商品名称
	 */
	@Column(name = "PRODUCT_NAME", nullable = false, length = 255, insertable = true, updatable = false)
	private String productName;

	/**
	 * 商户数据包
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 255, insertable = true, updatable = false)
	private String productDescription;

	/**
	 * 手续费
	 */
	@Column(name = "SERVICE_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 5)
	private Float serviceFee = 0f;

	/**
	 * 费率
	 */
	@Column(name = "SEVICE_RATE", nullable = false, length = 10, insertable = true, updatable = false, scale = 5)
	private Float serviceRate = 0f;

	public BillingSuccess() {

		super();
	}
	
	public BillingSuccess(String data) {

		super();
		String[] datas = StringUtils.split(data, ",");
		try {
			this.setTradingTime(dateFormat.parse(StringUtils.replaceAll(datas[0], "`", "")));
			this.setAppId(StringUtils.replaceAll(datas[1], "`", ""));
			this.setMachNo(StringUtils.replaceAll(datas[2], "`", ""));
			this.setSubMachNo(StringUtils.replaceAll(datas[3], "`", ""));
			this.setDeviceId(StringUtils.replaceAll(datas[4], "`", ""));
			this.setTransactionId(StringUtils.replaceAll(datas[5], "`", ""));
			this.setBillingId(StringUtils.replaceAll(datas[6], "`", ""));
			this.setOpenId(StringUtils.replaceAll(datas[7], "`", ""));
			this.setTradingType(StringUtils.replaceAll(datas[8], "`", ""));
			this.setTradingStatus(StringUtils.replaceAll(datas[9], "`", ""));
			this.setBank(StringUtils.replaceAll(datas[10], "`", ""));
			this.setCurrency(StringUtils.replaceAll(datas[11], "`", ""));
			this.setTotalFee(Float.parseFloat(StringUtils.replaceAll(datas[12], "`", "")));
			this.setDiscountFee(Float.parseFloat(StringUtils.replaceAll(datas[13], "`", "")));
			this.setProductName(StringUtils.replaceAll(datas[14], "`", ""));
			this.setProductDescription(StringUtils.replaceAll(datas[15], "`", ""));
			this.setServiceFee(Float.parseFloat(StringUtils.replaceAll(datas[16], "`", "")));
			String serviceRate = StringUtils.replaceAll(datas[17], "`", "");
			serviceRate = StringUtils.replaceAll(serviceRate, "%", "");
			this.setServiceRate(Float.parseFloat(serviceRate) / 100);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getTradingTime() {
		return tradingTime;
	}

	public void setTradingTime(Date tradingTime) {
		this.tradingTime = tradingTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMachNo() {
		return machNo;
	}

	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}

	public String getSubMachNo() {
		return subMachNo;
	}

	public void setSubMachNo(String subMachNo) {
		this.subMachNo = subMachNo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTradingType() {
		return tradingType;
	}

	public void setTradingType(String tradingType) {
		this.tradingType = tradingType;
	}

	public String getTradingStatus() {
		return tradingStatus;
	}

	public void setTradingStatus(String tradingStatus) {
		this.tradingStatus = tradingStatus;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Float totalFee) {
		this.totalFee = totalFee;
	}

	public Float getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(Float discountFee) {
		this.discountFee = discountFee;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Float getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Float serviceFee) {
		this.serviceFee = serviceFee;
	}

	public Float getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(Float serviceRate) {
		this.serviceRate = serviceRate;
	}

	@Override
	public String toString() {
		return "BillingSuccess [tradingTime=" + tradingTime + ", appId=" + appId + ", machNo=" + machNo + ", subMachNo="
				+ subMachNo + ", deviceId=" + deviceId + ", billingId=" + billingId + ", transactionId=" + transactionId
				+ ", openId=" + openId + ", tradingType=" + tradingType + ", tradingStatus=" + tradingStatus + ", bank="
				+ bank + ", currency=" + currency + ", totalFee=" + totalFee + ", discountFee=" + discountFee
				+ ", productName=" + productName + ", productDescription=" + productDescription + ", serviceFee="
				+ serviceFee + ", serviceRate=" + serviceRate + "]";
	}

}
