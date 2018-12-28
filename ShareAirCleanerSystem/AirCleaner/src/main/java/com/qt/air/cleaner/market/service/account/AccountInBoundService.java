package com.qt.air.cleaner.market.service.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.account.AccountInBound;
import com.qt.air.cleaner.market.vo.account.AccountInBoundView;

public interface AccountInBoundService {
	/**
	 * 入账记录分页查询
	 * 
	 * @param code
	 * @param name
	 * @param type
	 * @param amount
	 * @param weixin
	 * @param inBoundTime
	 * @param phoneNum
	 * @param state
	 * @return
	 */
	public Page<AccountInBound> findAllAccountInBound(AccountInBoundView accountInBoundView, Pageable pageable);
	
	/**
	 * 入账记录信息下拉列表
	 * 
	 * @param removed
	 * @return
	 */
	List<AccountInBound> findAll(boolean removed);
	
	public void updateState(String id);

}
