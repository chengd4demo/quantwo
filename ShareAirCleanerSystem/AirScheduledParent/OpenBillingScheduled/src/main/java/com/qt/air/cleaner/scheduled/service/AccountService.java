package com.qt.air.cleaner.scheduled.service;

import com.qt.air.cleaner.scheduled.domain.Billing;
import com.qt.air.cleaner.scheduled.domain.Company;
import com.qt.air.cleaner.scheduled.domain.Investor;
import com.qt.air.cleaner.scheduled.domain.Trader;

public interface AccountService {
	void udpateInvestorAccount(Billing billing,Investor investor,Float amount);
	void updateCompanyAccount(Billing billing,Company company,Float amount);
	void updateTraderAccount(Billing billing,Trader trader,Float amount);
}
