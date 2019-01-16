package com.qt.air.cleaner.scheduled.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "ACT_BILLING_WX_DOWNLOAD")
public class WeiXinDownload extends GenericEntity {

	private static final long serialVersionUID = -2428591912035563990L;

	public final static String WEIXIN_DOWNLOAD_TYPE_SUCCESS = "SUCCESS";

	public final static String WEIXIN_DOWNLOAD_TYPE_REFUND = "REFUND";

	/**
	 * 公众账号ID
	 */
	@Column(name = "APP_ID", nullable = false, length = 40, insertable = true, updatable = false)
	private String appId;

	/**
	 * 商户号
	 */
	@Column(name = "MCH_NO", nullable = false, length = 40, insertable = true, updatable = false)
	private String mchNo;

	@Column(name = "DOWNLOAD_DATE", nullable = false, insertable = true, updatable = false)
	@Temporal(TemporalType.DATE)
	private Date downloadDate;

	/**
	 * 下载类型
	 */
	@Column(name = "TYPE", nullable = false, length = 10, insertable = true, updatable = false)
	private String type;
	/**
	 * 总交易单数
	 */
	@Column(name = "TRADING_COUNT", nullable = false, length = 10, insertable = true, updatable = false)
	private Integer tradingCount = 0;

	/**
	 * 总交易额
	 */
	@Column(name = "SUCCESS_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 2)
	private Double successFee = 0D;

	/**
	 * 总退款金额
	 */
	@Column(name = "REFUND_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 2)
	private Double refundFee = 0D;

	/**
	 * 总代金券或立减优惠退款金额
	 */
	@Column(name = "DISCOUNT_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 2)
	private Double discountFee = 0D;

	/**
	 * 手续费总金额
	 */
	@Column(name = "SERVICE_FEE", nullable = false, length = 10, insertable = true, updatable = false, scale = 2)
	private Double serviceFee = 0D;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOWNLOAD_ID", insertable = true, updatable = true)
	private List<BillingSuccess> success;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOWNLOAD_ID", insertable = true, updatable = true)
	private List<BillingRefund> refund;

	@Column(name = "RETURN_MSG", nullable = false, length = 40, insertable = true, updatable = true)
	private String returnMsg;

	@Column(name = "RETURN_CODE", nullable = false, length = 40, insertable = true, updatable = true)
	private String returnCode;

	public WeiXinDownload() {
		super();
	}

	public void setData(String data) {
		String[] datas = StringUtils.split(data, ",");

		this.setTradingCount(Integer.parseInt(StringUtils.replaceAll(datas[0], "`", "")));
		this.setSuccessFee(Double.parseDouble(StringUtils.replaceAll(datas[1], "`", "")));
		this.setRefundFee(Double.parseDouble(StringUtils.replaceAll(datas[2], "`", "")));
		this.setDiscountFee(Double.parseDouble(StringUtils.replaceAll(datas[3], "`", "")));
		this.setServiceFee(Double.parseDouble(StringUtils.replaceAll(datas[4], "`", "")));

	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchNo() {
		return mchNo;
	}

	public void setMchNo(String mchNo) {
		this.mchNo = mchNo;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTradingCount() {
		return tradingCount;
	}

	public void setTradingCount(Integer tradingCount) {
		this.tradingCount = tradingCount;
	}

	public Double getSuccessFee() {
		return successFee;
	}

	public void setSuccessFee(Double successFee) {
		this.successFee = successFee;
	}

	public Double getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Double refundFee) {
		this.refundFee = refundFee;
	}

	public Double getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(Double discountFee) {
		this.discountFee = discountFee;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public List<BillingSuccess> getSuccess() {
		return success;
	}

	public void setSuccess(List<BillingSuccess> success) {
		this.success = success;
	}

	public List<BillingRefund> getRefund() {
		return refund;
	}

	public void setRefund(List<BillingRefund> refund) {
		this.refund = refund;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

}
