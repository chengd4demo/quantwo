package com.qt.air.cleaner.market.service.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.account.Billing;
import com.qt.air.cleaner.market.vo.account.BillingView;

public interface BillingService {
	/**
	 * 设备消费记录分页查询
	 * 
	 * @param billingId
	 * @param machNo
	 * @param deviceId
	 * @param priceId
	 * @param costTime
	 * @param unitPrice
	 * @param discountStr
	 * @param state
	 * @param errorCode
	 * @param errorMsg
	 * @param transactionId
	 * @param createTimeStr
	 * @return
	 */
	public Page<Billing> findAllBilling(BillingView billingView, Pageable pageable);
	
	/**
	 * 设备消费信息下拉列表
	 * 
	 * @param removed
	 * @return
	 */
	List<Billing> findAll(boolean removed);
}
