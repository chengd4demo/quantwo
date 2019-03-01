package com.qt.air.cleaner.scheduled.service;

public interface CashWithdrawalService {
	/**
	 * 发送现金红包
	 * 
	 */
	void sendRedWithdrawal();
	
	/**
	 * 更新领取状态
	 */
	void updateRedWithdrawalState();
}
