package com.qt.air.cleaner.scheduled.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.qt.air.cleaner.scheduled.domain.Billing;
import com.qt.air.cleaner.scheduled.domain.Company;
import com.qt.air.cleaner.scheduled.domain.Device;
import com.qt.air.cleaner.scheduled.domain.Investor;
import com.qt.air.cleaner.scheduled.domain.Trader;
import com.qt.air.cleaner.scheduled.repository.DeviceRepository;
import com.qt.air.cleaner.scheduled.service.AccountService;
import com.qt.air.cleaner.scheduled.service.BillingService;

public class BillingServiceImpl implements BillingService {
	@Resource
	DeviceRepository deviceRepository;
	@Autowired
	AccountService accountService;
	private Map<String, Integer> gainProportion = new HashMap<String, Integer>();
	@Value("${o2.billing.investor.gain.proportion}")
	public Integer investorGainProportion;
	@Value("${o2.billing.trader.gain.proportion}")
	public Integer traderGainProportion;
	@Value("${o2.billing.compnay.gain.proportion}")
	public Integer companyGainProportion;
	
	@PostConstruct
	public void init(){
		gainProportion.put(Investor.class.getSimpleName(), investorGainProportion);
		gainProportion.put(Trader.class.getSimpleName(), traderGainProportion);
		gainProportion.put(Company.class.getSimpleName(), companyGainProportion);
	}
	@Override
	public void openBillingAccount(Billing billing) {
		/**投资商开账逻辑*/
		Float totalAmount = billing.getAmount();
		Device device = deviceRepository.getOne(billing.getDeviceId());
		Investor investor = device.getInvestor();
		Integer proportion = gainProportion.get(Investor.class.getSimpleName());
		Float investorAmount = (totalAmount * proportion / 100) - ((billing.getCostTime() / 60) * 0.09f); // 每小时扣除0.09耗材费用
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(investorAmount));
		investorAmount = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		accountService.udpateInvestorAccount(billing, investor, investorAmount);
		
		/**商家开账逻辑*/
		Trader trader = device.getTrader();
		proportion = gainProportion.get(Trader.class.getSimpleName());
		Float traderAmount = totalAmount * proportion / 100;
		bigDecimal = new BigDecimal(String.valueOf(traderAmount));
		traderAmount = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		accountService.updateTraderAccount(billing, trader, traderAmount);
		
		/**公司开账逻辑*/
		Company company = device.getCompany();
		proportion = gainProportion.get(Company.class.getSimpleName());
		Float companyAmount = totalAmount * proportion / 100;
		if (companyAmount < (totalAmount - investorAmount - traderAmount)) {
			companyAmount = totalAmount - investorAmount - traderAmount;
			bigDecimal = new BigDecimal(String.valueOf(companyAmount));
			companyAmount = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		}
		accountService.updateCompanyAccount(billing, company, companyAmount);
	}

}
