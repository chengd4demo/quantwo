package com.qt.air.cleaner.market.vo.account;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.account.AccountOutBound;

public class AccountOutBoundView {

	private String id;
	private String name; //用户姓名
	private String phoneNumber; //手机号码
	private String nickName; //昵称
	private String timeEnd; //支付完成时间
	private String type; //出账类型
	private String cashMode; //支付类型
	private Float amount; //金额
	private Integer state; //账务状态	
	private String returnCode; //业务结果
	private String returnMsg; //错误信息
	private String errCode; //错误代码
	private String accountId; //关联对象Id
	private String accountName; //关联对象Name
	private String applyTime;	//申请时间
	private String errorMsg;
	private String method;
	private String rejectReason; //驳回原因
	private String rejectReasonId;	//驳回原因Id

	@JsonIgnore
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public AccountOutBoundView(AccountOutBound accountOutBound) {
		super();
		this.setId(accountOutBound.getId());
		this.setName(accountOutBound.getName());
		this.setPhoneNumber("");
		this.setNickName("");
		this.setTimeEnd(accountOutBound.getLastOperateTime() == null ? "" : df.format(accountOutBound.getLastOperateTime()));//不为空时再取值
		this.setType(accountOutBound.getType());
		this.setCashMode(accountOutBound.getCashModeStr());
		this.setAmount(accountOutBound.getAmount());
		this.setState(accountOutBound.getState());
		this.setErrorMsg(accountOutBound.getErrorMsg());
		this.setErrCode(accountOutBound.getErrorCode());
		this.setApplyTime(accountOutBound.getCreateTime() == null ? "" : df.format(accountOutBound.getCreateTime()));
		
		if(accountOutBound.getOutBoundRejectReason() != null){
			this.setRejectReason(accountOutBound.getOutBoundRejectReason().getRejectReason());
			this.setRejectReasonId(accountOutBound.getOutBoundRejectReason().getId());
		}
	}
	
	public AccountOutBoundView() {
		super();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getNickName() {
		return nickName;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public String getType() {
		return type;
	}

	public String getCashMode() {
		return cashMode;
	}

	public Float getAmount() {
		return amount;
	}

	public Integer getState() {
		return state;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getMethod() {
		return method;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCashMode(String cashMode) {
		this.cashMode = cashMode;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(String rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	@Override
	public String toString() {
		return "AccountOutBoundView [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + ", nickName="
				+ nickName + ", timeEnd=" + timeEnd + ", type=" + type + ", cashMode=" + cashMode + ", amount=" + amount
				+ ", state=" + state + ", returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", errCode="
				+ errCode + ", accountId=" + accountId + ", accountName=" + accountName + ", applyTime=" + applyTime
				+ ", errorMsg=" + errorMsg + ", method=" + method + ", rejectReason=" + rejectReason
				+ ", rejectReasonId=" + rejectReasonId + ", df=" + df + "]";
	}
		
	
}
