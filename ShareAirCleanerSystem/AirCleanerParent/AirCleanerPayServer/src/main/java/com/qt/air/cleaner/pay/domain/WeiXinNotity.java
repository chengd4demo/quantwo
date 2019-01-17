package com.qt.air.cleaner.pay.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.qt.air.cleaner.base.domain.GenericEntity;
import com.qt.air.cleaner.pay.utils.BankUtil;

public class WeiXinNotity extends GenericEntity {
	private static final long serialVersionUID = 557861056702960269L;
	@Transient
	// 微信通知业务处理异常状态
	public static final Integer WEIXIN_NOTITY_EXCEPTION = -2;
	
	@Transient
	// 新增的微信通知记录
	public static final Integer WEIXIN_NOTITY_NEW = 0;
	
	@Transient
	// 已与微信对账单完成对账操作，并可以进行开帐帐操作
	public static final Integer WEIXIN_NOTITY_CONFIRM = 1;
	
	@Transient
	// 已完成业务的开帐工作
	public static final Integer WEIXIN_NOTITY_OPEN = 2;
	
	@Transient
	// 对应的微信通知记录产生了退款记录
	public static final Integer WEIXIN_NOTITY_BACK = -1;
	
	//@Transient
	//public static final Integer WEIXIN_NOTITY_COMPLETE = 3;
	
	@Transient
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Transient
	public SimpleDateFormat weiXinDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
	
	@Column(name = "RETURN_CODE", length = 16, insertable = true, updatable = false)
	private String returnCode;
	
	@Column(name = "RETURN_MSG", length = 128, insertable = true, updatable = false)
	private String returnMsg;
	
	@Column(name = "APP_ID", length = 32, insertable = true, updatable = false)
	private String appId;
	
	@Column(name = "MCH_ID", length = 32, insertable = true, updatable = false)
	private String mchId;
	
	@Column(name = "DEVICE_INFO", length = 32, insertable = true, updatable = false)
	private String deviceInfo;
	
	@Column(name = "NONCE_STR", length = 32, insertable = true, updatable = false)
	private String nonceStr;
	
	@Column(name = "SIGN", length = 32, insertable = true, updatable = false)
	private String sign;
	
	@Column(name = "SIGN_TYPE", length = 32, insertable = true, updatable = false)
	private String signType;
	
	@Column(name = "RESULT_CODE", length = 16, insertable = true, updatable = false)
	private String resultCode;
	
	@Column(name = "ERR_CODE", length = 32, insertable = true, updatable = false)
	private String errCode;
	
	@Column(name = "ERR_CODE_DES", length = 128, insertable = true, updatable = true)
	private String errCodeDes;
	
	@Column(name = "OPER_ID", length = 128, insertable = true, updatable = false)
	private String openId;
	
	@Column(name = "IS_SUBSCRIBE", length = 1, insertable = true, updatable = false)
	private String isSubscribe;
	
	@Column(name = "TRADE_TYPE", length = 16, insertable = true, updatable = false)
	private String tradeType;
	
	@Column(name = "BANK_TYPE", length = 16, insertable = true, updatable = false)
	private String bankType;
	
	@Transient
	private String bankTypeStr;
	
	@Column(name = "TOTAL_FEE", insertable = true, updatable = false)
	private Float totalFee;
	
	@Column(name = "SETTLEMENT_TOTAL_FEE", insertable = true, updatable = false)
	private Float settlementTotalFee;
	
	@Column(name = "FEE_TYPE", length = 8, insertable = true, updatable = false)
	private String feeType;
	
	@Column(name = "CASH_FEE", insertable = true, updatable = false)
	private Float cashFee;
	
	@Column(name = "CASH_FEE_TYPE", length = 16, insertable = true, updatable = false)
	private String cashFeeType;
	
	@Column(name = "COUPON_FEE", insertable = true, updatable = false)
	private Float couponFee;
	
	@Column(name = "COUPON_COUNT", insertable = true, updatable = false)
	private Integer couponCount;
	
	@Column(name = "COUPON_TYPE_N", length = 8, insertable = true, updatable = false)
	private String couponTypeN;
	
	@Column(name = "COUPON_ID_N", length = 20, insertable = true, updatable = false)
	private String couponIdN;
	
	@Column(name = "COUPON_FEE_N", insertable = true, updatable = false)
	private Float couponFeeN;
	
	@Column(name = "TRANSACTION_ID", length = 32, insertable = true, updatable = false)
	private String transactionId;
	
	@Column(name = "OUT_TRADE_NO", length = 32, insertable = true, updatable = false)
	private String outTradeNo;
	
	@Column(name = "ATTACH", length = 128, insertable = true, updatable = false)
	private String attach;
	
	@Column(name = "TIME_END", length = 14, insertable = true, updatable = false)
	private String timeEnd;
	
	@Column(name = "STATE")
	private Integer state = WEIXIN_NOTITY_NEW;
	
	@Transient
	private String timeEndStr;
	
	public String getReturnCode() {
		
		return returnCode;
	}
	
	public void setReturnCode(String returnCode) {
		
		this.returnCode = returnCode;
	}
	
	public String getReturnMsg() {
		
		return returnMsg;
	}
	
	public void setReturnMsg(String returnMsg) {
		
		this.returnMsg = returnMsg;
	}
	
	public String getAppId() {
		
		return appId;
	}
	
	public void setAppId(String appId) {
		
		this.appId = appId;
	}
	
	public String getMchId() {
		
		return mchId;
	}
	
	public void setMchId(String mchId) {
		
		this.mchId = mchId;
	}
	
	public String getDeviceInfo() {
		
		return deviceInfo;
	}
	
	public void setDeviceInfo(String deviceInfo) {
		
		this.deviceInfo = deviceInfo;
	}
	
	public String getNonceStr() {
		
		return nonceStr;
	}
	
	public void setNonceStr(String nonceStr) {
		
		this.nonceStr = nonceStr;
	}
	
	public String getSign() {
		
		return sign;
	}
	
	public void setSign(String sign) {
		
		this.sign = sign;
	}
	
	public String getSignType() {
		
		return signType;
	}
	
	public void setSignType(String signType) {
		
		this.signType = signType;
	}
	
	public String getResultCode() {
		
		return resultCode;
	}
	
	public void setResultCode(String resultCode) {
		
		this.resultCode = resultCode;
	}
	
	public String getErrCode() {
		
		return errCode;
	}
	
	public void setErrCode(String errCode) {
		
		this.errCode = errCode;
	}
	
	public String getErrCodeDes() {
		
		return errCodeDes;
	}
	
	public void setErrCodeDes(String errCodeDes) {
		
		this.errCodeDes = errCodeDes;
	}
	
	public String getOpenId() {
		
		return openId;
	}
	
	public void setOpenId(String openId) {
		
		this.openId = openId;
	}
	
	public String getIsSubscribe() {
		
		return isSubscribe;
	}
	
	public void setIsSubscribe(String isSubscribe) {
		
		this.isSubscribe = isSubscribe;
	}
	
	public String getTradeType() {
		
		return tradeType;
	}
	
	public void setTradeType(String tradeType) {
		
		this.tradeType = tradeType;
	}
	
	public String getBankType() {
		
		return bankType;
	}
	
	public void setBankType(String bankType) {
		
		this.bankType = bankType;
	}
	
	public Float getTotalFee() {
		
		return totalFee;
	}
	
	public void setTotalFee(Float totalFee) {
		
		this.totalFee = totalFee;
	}
	
	public Float getSettlementTotalFee() {
		
		return settlementTotalFee;
	}
	
	public void setSettlementTotalFee(Float settlementTotalFee) {
		
		this.settlementTotalFee = settlementTotalFee;
	}
	
	public String getFeeType() {
		
		return feeType;
	}
	
	public void setFeeType(String feeType) {
		
		this.feeType = feeType;
	}
	
	public Float getCashFee() {
		
		return cashFee;
	}
	
	public void setCashFee(Float cashFee) {
		
		this.cashFee = cashFee;
	}
	
	public String getCashFeeType() {
		
		return cashFeeType;
	}
	
	public void setCashFeeType(String cashFeeType) {
		
		this.cashFeeType = cashFeeType;
	}
	
	public Float getCouponFee() {
		
		return couponFee;
	}
	
	public void setCouponFee(Float couponFee) {
		
		this.couponFee = couponFee;
	}
	
	public Integer getCouponCount() {
		
		return couponCount;
	}
	
	public void setCouponCount(Integer couponCount) {
		
		this.couponCount = couponCount;
	}
	
	public String getCouponTypeN() {
		
		return couponTypeN;
	}
	
	public void setCouponTypeN(String couponTypeN) {
		
		this.couponTypeN = couponTypeN;
	}
	
	public String getCouponIdN() {
		
		return couponIdN;
	}
	
	public void setCouponIdN(String couponIdN) {
		
		this.couponIdN = couponIdN;
	}
	
	public Float getCouponFeeN() {
		
		return couponFeeN;
	}
	
	public void setCouponFeeN(Float couponFeeN) {
		
		this.couponFeeN = couponFeeN;
	}
	
	public String getTransactionId() {
		
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		
		this.transactionId = transactionId;
	}
	
	public String getOutTradeNo() {
		
		return outTradeNo;
	}
	
	public void setOutTradeNo(String outTradeNo) {
		
		this.outTradeNo = outTradeNo;
	}
	
	public String getAttach() {
		
		return attach;
	}
	
	public void setAttach(String attach) {
		
		this.attach = attach;
	}
	
	public String getTimeEnd() {
		
		return timeEnd;
	}
	
	public void setTimeEnd(String timeEnd) {
		
		this.timeEnd = timeEnd;
	}
	
	public Integer getState() {
		
		return state;
	}
	
	public void setState(Integer state) {
		
		this.state = state;
	}
	
	public static WeiXinNotity toWeiXinNotity(Map<String, String> xmlData) {
		
		WeiXinNotity notity = new WeiXinNotity();
		if (xmlData.containsKey("return_code")) {
			notity.setReturnCode(xmlData.get("return_code"));
		}
		if (xmlData.containsKey("return_msg")) {
			notity.setReturnMsg(xmlData.get("return_msg"));
		}
		if (xmlData.containsKey("appid")) {
			notity.setAppId(xmlData.get("appid"));
		}
		if (xmlData.containsKey("mch_id")) {
			notity.setMchId(xmlData.get("mch_id"));
		}
		if (xmlData.containsKey("device_info")) {
			notity.setDeviceInfo(xmlData.get("device_info"));
		}
		if (xmlData.containsKey("nonce_str")) {
			notity.setNonceStr(xmlData.get("nonce_str"));
		}
		if (xmlData.containsKey("sign")) {
			notity.setSign(xmlData.get("sign"));
		}
		if (xmlData.containsKey("sign_type")) {
			notity.setSignType(xmlData.get("sign_type"));
		}
		if (xmlData.containsKey("result_code")) {
			notity.setResultCode(xmlData.get("result_code"));
		}
		if (xmlData.containsKey("err_code")) {
			notity.setErrCode(xmlData.get("err_code"));
		}
		if (xmlData.containsKey("err_code_des")) {
			notity.setErrCodeDes(xmlData.get("err_code_des"));
		}
		if (xmlData.containsKey("openid")) {
			notity.setOpenId(xmlData.get("openid"));
		}
		if (xmlData.containsKey("is_subscribe")) {
			notity.setIsSubscribe(xmlData.get("is_subscribe"));
		}
		if (xmlData.containsKey("trade_type")) {
			notity.setTradeType(xmlData.get("trade_type"));
		}
		if (xmlData.containsKey("bank_type")) {
			notity.setBankType(xmlData.get("bank_type"));
		}
		if (xmlData.containsKey("total_fee")) {
			String totalFeeStr = xmlData.get("total_fee");
			Float totalFee = Float.parseFloat(totalFeeStr);
			notity.setTotalFee(totalFee / 100);
		}
		if (xmlData.containsKey("settlement_total_fee")) {
			String totalFeeStr = xmlData.get("settlement_total_fee");
			Float totalFee = Float.parseFloat(totalFeeStr);
			notity.setSettlementTotalFee(totalFee / 100);
		}
		if (xmlData.containsKey("fee_type")) {
			notity.setFeeType(xmlData.get("fee_type"));
		}
		if (xmlData.containsKey("cash_fee")) {
			String cashFeeStr = xmlData.get("cash_fee");
			Float cashFee = Float.parseFloat(cashFeeStr);
			notity.setCashFee(cashFee / 100);
		}
		if (xmlData.containsKey("cash_fee_type")) {
			notity.setCashFeeType(xmlData.get("cash_fee_type"));
		}
		if (xmlData.containsKey("coupon_fee")) {
			String couponFeeStr = xmlData.get("coupon_fee");
			Float couponFee = Float.parseFloat(couponFeeStr);
			notity.setCouponFee(couponFee / 100);
		}
		if (xmlData.containsKey("coupon_count")) {
			String couponCountStr = xmlData.get("coupon_count");
			Integer couponCount = Integer.parseInt(couponCountStr);
			notity.setCouponCount(couponCount);
		}
		if (xmlData.containsKey("coupon_id_$n")) {
			notity.setCouponIdN(xmlData.get("coupon_id_$n"));
		}
		if (xmlData.containsKey("coupon_fee_$n")) {
			String couponFeeStr = xmlData.get("coupon_fee_$n");
			Float couponFee = Float.parseFloat(couponFeeStr);
			notity.setCouponFeeN(couponFee / 100);
		}
		if (xmlData.containsKey("transaction_id")) {
			notity.setTransactionId(xmlData.get("transaction_id"));
		}
		if (xmlData.containsKey("out_trade_no")) {
			notity.setOutTradeNo(xmlData.get("out_trade_no"));
		}
		if (xmlData.containsKey("attach")) {
			notity.setAttach(xmlData.get("attach"));
		}
		if (xmlData.containsKey("time_end")) {
			notity.setTimeEnd(xmlData.get("time_end"));
		}
		return notity;
	}
	
	public String getBankTypeStr() {
		
		return BankUtil.getbankType(this.getBankType());
	}
	
	public void setBankTypeStr(String bankTypeStr) {
		
		this.bankTypeStr = bankTypeStr;
	}
	
	public String getTimeEndStr() {
		
		try {
			Date weixinDate = weiXinDateFormat.parse(this.getTimeEnd());
			return dateFormat.format(weixinDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return this.getTimeEnd();
		}
	}
	
	public void setTimeEndStr(String timeEndStr) {
		
		this.timeEndStr = timeEndStr;
	}
	
	@Override
	public String toString() {
		
		return "WeiXinNotity [returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", appId=" + appId + ", mchId="
		        + mchId + ", deviceInfo=" + deviceInfo + ", nonceStr=" + nonceStr + ", sign=" + sign + ", signType="
		        + signType + ", resultCode=" + resultCode + ", errCode=" + errCode + ", errCodeDes=" + errCodeDes
		        + ", openId=" + openId + ", isSubscribe=" + isSubscribe + ", tradeType=" + tradeType + ", bankType="
		        + bankType + ", totalFee=" + totalFee + ", settlementTotalFee=" + settlementTotalFee + ", feeType="
		        + feeType + ", cashFee=" + cashFee + ", cashFeeType=" + cashFeeType + ", couponFee=" + couponFee
		        + ", couponCount=" + couponCount + ", couponTypeN=" + couponTypeN + ", couponIdN=" + couponIdN
		        + ", couponFeeN=" + couponFeeN + ", transactionId=" + transactionId + ", outTradeNo=" + outTradeNo
		        + ", attach=" + attach + ", timeEnd=" + timeEnd + "]";
	}
}
