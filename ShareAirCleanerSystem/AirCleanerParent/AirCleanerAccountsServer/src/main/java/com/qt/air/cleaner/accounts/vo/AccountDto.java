package com.qt.air.cleaner.accounts.vo;

import java.io.Serializable;

import com.qt.air.cleaner.accounts.domain.Account;
import com.qt.air.cleaner.accounts.domain.Agent;
import com.qt.air.cleaner.accounts.domain.Company;
import com.qt.air.cleaner.accounts.domain.Investor;
import com.qt.air.cleaner.accounts.domain.Trader;

public class AccountDto implements Serializable {
	private static final long serialVersionUID = 6835862280982173503L;

	private String identificationNumber;

	private String name;

	private String address;

	private String weixin;

	private Float totalAmount = 0.00f;

	private Float freezingAmount = 0.00f;

	private Float availableAmount = 0.00f;
	
	private String accountType;

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Float getFreezingAmount() {
		return freezingAmount;
	}

	public void setFreezingAmount(Float freezingAmount) {
		this.freezingAmount = freezingAmount;
	}

	public Float getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Float availableAmount) {
		this.availableAmount = availableAmount;
	}
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public AccountDto(Trader trader, Account account) {
		super();
		this.setIdentificationNumber(trader.getSocialCreditCode());
		this.setName(trader.getName());
		this.setAddress(trader.getAddress());
		this.setWeixin(trader.getWeixin());
		this.setTotalAmount(account.getTotalAmount());
		this.setAvailableAmount(account.getAvailableAmount());
		this.setFreezingAmount(account.getFreezingAmount());
		this.setAccountType(account.getAccountType());
	}

	public AccountDto(Investor investor, Account account) {
		super();
		this.setIdentificationNumber(investor.getIdentificationNumber());
		this.setName(investor.getName());
		this.setAddress(investor.getAddress());
		this.setWeixin(investor.getWeixin());
		this.setTotalAmount(account.getTotalAmount());
		this.setAvailableAmount(account.getAvailableAmount());
		this.setFreezingAmount(account.getFreezingAmount());
		this.setAccountType(account.getAccountType());
	}

	public AccountDto(Company company, Account account) {
		super();
		this.setIdentificationNumber(company.getSocialCreditCode());
		this.setName(company.getName());
		this.setAddress(company.getAddress());
		this.setWeixin(company.getWeixin());
		this.setTotalAmount(account.getTotalAmount());
		this.setAvailableAmount(account.getAvailableAmount());
		this.setFreezingAmount(account.getFreezingAmount());
		this.setAccountType(account.getAccountType());
	}
	
	public AccountDto(Agent agent, Account account) {
		super();
		this.setIdentificationNumber(agent.getIdentificationNumber());
		this.setName(agent.getName());
		this.setAddress(agent.getAddress());
		this.setWeixin(agent.getWeixin());
		this.setTotalAmount(account.getTotalAmount());
		this.setAvailableAmount(account.getAvailableAmount());
		this.setFreezingAmount(account.getFreezingAmount());
		this.setAccountType(account.getAccountType());
	}
}
