package com.qt.air.cleaner.market.service.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.account.BillingRefund;
import com.qt.air.cleaner.market.vo.account.BillingRefundView;

public interface BillingRefundService {
	/**
	 * 退款记录记录分页查询
	 * 
	 * @param name
	 * @param phoneNum
	 * @param nickName
	 * @param amount
	 * @param createTime
	 * @param refundType
	 * @param totalFee
	 * @param refundStatus
	 * @return
	 */
	public Page<BillingRefund> findAllBillingRefund(BillingRefundView billingRefundView,Pageable pageable);
	
	public void updateState(BillingRefundView billingRefundView);

	public BillingRefund findByIdRemoved(String id, boolean b);
}
