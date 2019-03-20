
package com.qt.air.cleaner.market.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qt.air.cleaner.utils.CashModeUtil;
import com.singalrain.framework.core.bo.GenericEntity;

/**
 * @ClassName: AccountOutBound
 * @Description: 账户出账记录
 * @author: Wonders-Rain
 * @date: 2018年10月3日 下午10:30:26
 * @version [版本号, YYYY-MM-DD]
 * @see       [相关类/方法]
 * @since     [产品/模块版本]
 */
@Entity
@Table(name = "ACT_ACCOUNT_OUTBOUND")
public class AccountOutBound extends GenericEntity {
	
	private static final long serialVersionUID = 6409421446743382719L;
	
		// 出账记录状态 -- 错误状态，无效的出账记录
		public static final Integer ACCOUNT_OUT_BOUND_STATE_ERROR = -1;
		
		// 出账记录状态 -- 初始化状态，正在执行出账工作 |出账记录状态-- 未审核
		public static final Integer ACCOUNT_OUT_BOUND_STATE_INIT = 0;
		
		// 出账记录状态 -- 完成状态，已完成的出账记录
		public static final Integer ACCOUNT_OUT_BOUND_STATE_COMPLETE = 1;
		
		//出账记录状态-- 已审核
		public static final Integer ACCOUNT_OUT_BOUND_STATE_AUDIT = 2;
		
		//出账记录状态-- 未通过
		public static final Integer ACCOUNT_OUT_BOUND_STATE_AUDITED = 3;
		
		//出账记录状态-- 已领取
		public static final Integer ACCOUNT_OUT_BOUND_STATE_RECEIVE = 4;
		
		//出账记录状态-- 发送失败
		public static final Integer ACCOUNT_OUT_BOUND_STATE_FAIL = 5;
		
		//出账记录状态-- 未领取
		public static final Integer ACCOUNT_OUT_BOUND_STATE_UNCLAIMED = 6;
		
		//出账记录状态-- 等待领取
		public static final Integer ACCOUNT_OUT_BOUND_STATE_WATINT_RECEVIE = 7;
		
		//出账记录状态-- 发放中
		public static final Integer ACCOUNT_OUT_BOUND_STATE_SEND_ING = 8;

		//出账记录状态-- 已取消
		public static final Integer ACCOUNT_OUT_BOUND_STATE_CANCEL = 9;
		
		// 提现方式--红包
		public static final Integer ACCOUNT_OUT_BOUND_MODE_REDPACK = 0;
		
		// 提现方式-转账
		public static final Integer ACCOUNT_OUT_BOUND_MODE_TRANSFER = 1;
	
	@Transient
	private String cashModeStr;
	
	/**
	 * @fieldName: account
	 * @fieldType: com.singalrain.market.bo.account.Account
	 * @Description: 关联的主账户对象
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	/**
	 * @fieldName: billingNumber
	 * @fieldType: java.lang.String
	 * @Description: 商户订单号（每个订单号必须唯一）组成：mch_id+yyyymmdd+10位一天内不能重复的数字
	 */
	@Column(name = "BILLING_NUMBER", length = 28, nullable = false, insertable = true, updatable = false)
	private String billingNumber;
	
	/**
	 * @fieldName: cashMode
	 * @fieldType: java.lang.Integer
	 * @Description: 当前提现方式，主要分为红包方式和转账方式，目前提供的是红包方式
	 */
	@Column(name = "CASH_MODE", nullable = false, insertable = true, updatable = false)
	private Integer cashMode;
	
	/**
	 * @fieldName: type
	 * @fieldType: java.lang.String
	 * @Description: 出账信息类型，根据业务要素主要分为公司、投资商、商户和销售类型
	 */
	@Column(name = "TYPE", length = 32, nullable = false, insertable = true, updatable = false)
	private String type;
	
	/**
	 * @fieldName: code
	 * @fieldType: java.lang.String
	 * @Description: 业务要素识别代码
	 */
	@Column(name = "CODE", length = 255, nullable = false, insertable = true, updatable = false)
	private String code;
	
	/**
	 * @fieldName: name
	 * @fieldType: java.lang.String
	 * @Description: 业务要素名称
	 */
	@Column(name = "NAME", length = 255, nullable = false, insertable = true, updatable = false)
	private String name;
	
	/**
	 * @fieldName: amount
	 * @fieldType: java.lang.Float
	 * @Description: 付款金额，单位分
	 */
	@Column(name = "AMOUNT", precision = 10, scale = 2, nullable = false, insertable = true, updatable = false)
	private Float amount;
	
	/**
	 * @fieldName: weixin
	 * @fieldType: java.lang.String
	 * @Description: 接受红包的用户openid，openid为用户在wxappid下的唯一标识
	 */
	@Column(name = "WEIXIN", nullable = false, length = 40)
	private String weixin;
	
	/**
	 * @fieldName: transactionId
	 * @fieldType: java.lang.String
	 * @Description: 订单的微信单号
	 */
	@Column(name = "TRANSACTION_ID", nullable = true, length = 32)
	private String transactionId;
	
	/**
	 * @fieldName: state
	 * @fieldType: java.lang.Integer
	 * @Description: 出账记录状态代码，0：初始状态；1：完成状态；-1：错误状态，为无效记录 
	 */
	@Column(name = "STATE", nullable = false)
	private Integer state = ACCOUNT_OUT_BOUND_STATE_INIT;
	
	/**
	 * @fieldName: errorCode
	 * @fieldType: java.lang.String
	 * @Description: 错误码信息
	 */
	@Column(name = "ERROR_CODE", nullable = true, length = 32)
	private String errorCode;
	
	/**
	 * @fieldName: errorMsg
	 * @fieldType: java.lang.String
	 * @Description: 结果信息描述
	 */
	@Column(name = "ERROR_MSG", nullable = true, length = 128)
	private String errorMsg;
	
	@OneToOne(fetch = FetchType.LAZY)
	private OutBoundRejectReason outBoundRejectReason;
	
	public OutBoundRejectReason getOutBoundRejectReason() {
		return outBoundRejectReason;
	}

	public void setOutBoundRejectReason(OutBoundRejectReason outBoundRejectReason) {
		this.outBoundRejectReason = outBoundRejectReason;
	}

	public Account getAccount() {
		
		return account;
	}
	
	public void setAccount(Account account) {
		
		this.account = account;
	}
	
	public String getBillingNumber() {
		
		return billingNumber;
	}
	
	public void setBillingNumber(String billingNumber) {
		
		this.billingNumber = billingNumber;
	}
	
	public String getType() {
		
		return type;
	}
	
	public void setType(String type) {
		
		this.type = type;
	}
	
	public String getCode() {
		
		return code;
	}
	
	public void setCode(String code) {
		
		this.code = code;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public Float getAmount() {
		
		return amount;
	}
	
	public void setAmount(Float amount) {
		
		this.amount = amount;
	}
	
	public String getWeixin() {
		
		return weixin;
	}
	
	public void setWeixin(String weixin) {
		
		this.weixin = weixin;
	}
	
	public Integer getState() {
		
		return state;
	}
	
	public void setState(Integer state) {
		
		this.state = state;
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
	
	public Integer getCashMode() {
		
		return cashMode;
	}
	
	public void setCashMode(Integer cashMode) {
		
		this.cashMode = cashMode;
	}

	public String getCashModeStr() {
		return CashModeUtil.getCashMode(this.getCashMode());
	}

	public void setCashModeStr(String cashModeStr) {
		this.cashModeStr = cashModeStr;
	}
	
}
