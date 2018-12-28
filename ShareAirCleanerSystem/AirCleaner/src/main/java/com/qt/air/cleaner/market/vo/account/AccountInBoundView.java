package com.qt.air.cleaner.market.vo.account;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.qt.air.cleaner.market.domain.account.AccountInBound;

public class AccountInBoundView implements Serializable {
	private static final long serialVersionUID = 8773796144126424859L;
	private String id;
	private String code;//用户编号
	private String name;//	用户名
	private String type;//	用户类型
	private Float amount;//	金额
	private String weixin;//	微信号
	private String inBoundTime;//	入账时间
	private String phoneNum;//	手机号
	private Integer state;//	状态
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
	public AccountInBoundView(AccountInBound accountInBound) {
		super();
		this.setId(accountInBound.getId());
		this.setCode(accountInBound.getCode());
		this.setName(accountInBound.getName());
		this.setType(accountInBound.getType());
		this.setAmount(accountInBound.getAmount());
		this.setWeixin(accountInBound.getWeixin());
		this.setInBoundTime(accountInBound.getCreateTime() == null ? "" : df.format(accountInBound.getCreateTime()));//不为空时再取值
		this.setState(accountInBound.getState());
	}
	
	public AccountInBoundView() {
		super();
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Float getAmount() {
		return amount;
	}

	public String getWeixin() {
		return weixin;
	}

	public String getInBoundTime() {
		return inBoundTime;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public Integer getState() {
		return state;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public void setInBoundTime(String inBoundTime) {
		this.inBoundTime = inBoundTime;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "AccountInBoundView [id=" + id + ", code=" + code + ", name=" + name + ", type=" + type + ", amount="
				+ amount + ", weixin=" + weixin + ", inBoundTime=" + inBoundTime + ", state=" + state + "]";
	}

		
	
	
}
