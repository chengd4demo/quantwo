package com.qt.air.cleaner.market.service.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.account.AccountOutBound;
import com.qt.air.cleaner.market.vo.account.AccountOutBoundView;

public interface AccountOutBoundService {

	/**
	 * 出账记录分页查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<AccountOutBound> findAllAccountOutBound(AccountOutBoundView AccountOutBoundView, Pageable pageable);

	/** 
	* @Title: saveOrUpdate 
	* @Description: 审核提现状态修改 
	* @param @param accountOutBoundView 
	* @return void 
	* @throws 
	*/ 
	public void updateState(AccountOutBoundView accountOutBoundView);
	
	public AccountOutBound findByIdRemoved(String id, boolean b);
}
