package com.qt.air.cleaner.market.vo.account;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.account.WeiXinNotity;

public class WeiXinNotityView implements Serializable {
	private static final long serialVersionUID = 7926372265066601388L;
	private String id;
	private String outTradeNo; //商户订单号
	private String deviceInfo; //设备编码
	private String transactionId; //支付订单号
	private String timeEnd; //支付完成时间
	private String bankType; //支付类型
	private Float cashFee; //金额
	private Integer state; //账务状态
	private String returnCode; //业务结果
	private String returnMsg; //错误信息
	private String errCode; //错误编码
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public WeiXinNotityView() {
		super();
	}
	public WeiXinNotityView(WeiXinNotity weiXinNotity) {
		super();
		this.setId(weiXinNotity.getId());
		this.setOutTradeNo(weiXinNotity.getOutTradeNo());
		this.setDeviceInfo(weiXinNotity.getDeviceInfo());
		this.setTransactionId(weiXinNotity.getTransactionId());
		if(StringUtils.isNotBlank(weiXinNotity.getTimeEnd())) {
			this.setTimeEnd(dateFormat.format(formatDate(weiXinNotity.getTimeEnd(),"yyyyMMddHHmmss")));
		}
		this.setBankType(weiXinNotity.getBankTypeStr());
		this.setCashFee(weiXinNotity.getCashFee());
		this.setState(weiXinNotity.getState());
		this.setReturnCode(weiXinNotity.getResultCode());
		this.setReturnMsg(weiXinNotity.getReturnMsg());
		this.setErrCode(weiXinNotity.getErrCode());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public Float getCashFee() {
		return cashFee;
	}
	public void setCashFee(Float cashFee) {
		this.cashFee = cashFee;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	private Date formatDate(String strDate,String pattern) {

        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(pattern);
        Date date = null;
        try {
        	date = dateTimeFormatter.parse(strDate);
			
		} catch (ParseException e) {
			System.out.print(e.getMessage());
		}
        return date;
    }
	@Override
	public String toString() {
		return "WeiXinNotityView [id=" + id + ", outTradeNo=" + outTradeNo + ", deviceInfo=" + deviceInfo
				+ ", transactionId=" + transactionId + ", timeEnd=" + timeEnd + ", bankType=" + bankType + ", cashFee="
				+ cashFee + ", state=" + state + ", returnCode=" + returnCode + ", returnMsg=" + returnMsg
				+ ", errCode=" + errCode + ", dateFormat=" + dateFormat + "]";
	}
				
}