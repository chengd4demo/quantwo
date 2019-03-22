package com.qt.air.cleaner.scheduled.service.impl;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.base.utils.CalculateUtils;
import com.qt.air.cleaner.scheduled.domain.Account;
import com.qt.air.cleaner.scheduled.domain.AccountInBound;
import com.qt.air.cleaner.scheduled.domain.Agent;
import com.qt.air.cleaner.scheduled.domain.Billing;
import com.qt.air.cleaner.scheduled.domain.Company;
import com.qt.air.cleaner.scheduled.domain.Investor;
import com.qt.air.cleaner.scheduled.domain.ShareProfit;
import com.qt.air.cleaner.scheduled.domain.Trader;
import com.qt.air.cleaner.scheduled.repository.AccountInBoundRepository;
import com.qt.air.cleaner.scheduled.repository.AccountRepository;
import com.qt.air.cleaner.scheduled.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	protected Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Resource
	AccountInBoundRepository accountInBoundRepository;
	@Resource
	AccountRepository accountRepository;
	@Override
	public void udpateInvestorAccount(Billing billing, Investor investor, Float amount) {
		logger.debug("账单[" + billing.getBillingId() + "]投资商[" + investor.getName() + "]分得金额:" + amount);
		Account account = investor.getAccount();
		AccountInBound inBound = new AccountInBound();
		inBound.setType("投资商");
		inBound.setCode(investor.getIdentificationNumber());
		inBound.setName(investor.getName());
		inBound.setWeixin(investor.getWeixin());
		inBound.setBilling(billing);
		inBound.setAmount(amount);
		inBound.setAccount(account);
		inBound.setCreater("defalut");
		inBound.setCreateTime(Calendar.getInstance().getTime());
		accountInBoundRepository.saveAndFlush(inBound);
		account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(),amount));
		account.setFreezingAmount(CalculateUtils.add(account.getFreezingAmount(),amount));
		accountRepository.saveAndFlush(account);
		
	}

	@Override
	public void updateCompanyAccount(Billing billing, Company company, Float amount) {
		logger.debug("账单[" + billing.getBillingId() + "]运营公司[" + company.getName() + "]分得金额:" + amount);
		Account account = company.getAccount();
		AccountInBound inBound = new AccountInBound();
		inBound.setType("运营商");
		inBound.setCode(company.getSocialCreditCode());
		inBound.setName(company.getName());
		inBound.setWeixin(company.getWeixin());
		inBound.setBilling(billing);
		inBound.setAmount(amount);
		inBound.setAccount(account);
		inBound.setCreater("defalut");
		inBound.setCreateTime(Calendar.getInstance().getTime());
		accountInBoundRepository.saveAndFlush(inBound);
		account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(),amount));
		account.setFreezingAmount(CalculateUtils.add(account.getFreezingAmount(),amount));
		accountRepository.saveAndFlush(account);
	}

	@Override
	public void updateTraderAccount(Billing billing, Trader trader, Float amount) {
		logger.debug("账单[" + billing.getBillingId() + "]商户[" + trader.getName() + "]分得金额:" + amount);
		Account account = trader.getAccount();
		AccountInBound inBound = new AccountInBound();
		inBound.setType("商户");
		inBound.setCode(trader.getSocialCreditCode());
		inBound.setName(trader.getName());
		inBound.setWeixin(trader.getWeixin());
		inBound.setBilling(billing);
		inBound.setAmount(amount);
		inBound.setAccount(account);
		inBound.setCreater("defalut");
		inBound.setCreateTime(Calendar.getInstance().getTime());
		accountInBoundRepository.save(inBound);
		account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(),amount));
		account.setFreezingAmount(CalculateUtils.add(account.getFreezingAmount(),amount));
		accountRepository.saveAndFlush(account);
	}

	@Override
	public void updateAgentAccount(Billing billing,Agent agent, Float amount) {
		logger.debug("账单[" + billing.getBillingId() + "]代理商[" + agent.getName() + "]分得金额:" + amount);
		Account account = agent.getAccount();
		AccountInBound inBound = new AccountInBound();
		String type = StringUtils.equals(ShareProfit.ACCOUNT_TYPE_AGENT_DL, agent.getType()) ? "代理" : "区域总代理";
		inBound.setType(type);
		inBound.setCode(agent.getIdentificationNumber());
		inBound.setName(agent.getName());
		inBound.setWeixin(agent.getWeixin());
		inBound.setBilling(billing);
		account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(),amount));
		account.setFreezingAmount(CalculateUtils.add(account.getFreezingAmount(),amount));
		inBound.setCreater("defalut");
		
	}

}
