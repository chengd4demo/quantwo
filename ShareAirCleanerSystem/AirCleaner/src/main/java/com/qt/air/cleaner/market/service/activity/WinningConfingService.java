package com.qt.air.cleaner.market.service.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.account.WinningConfing;
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;
import com.qt.air.cleaner.market.vo.activity.WinningConfingView;
import com.qt.air.cleaner.market.vo.generic.DeviceBatchView;

public interface WinningConfingService {
	/**
	 * 中奖名单分页模糊查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<WinningConfing> findAllWinningConfing(WinningConfingView params, Pageable pageable);
	

	
	/**
	 * 发货信息更新或保存
	 * 
	 * @param winningConfing
	 * @throws BusinessException
	 */
	public void saveOrUpdate(WinningConfingView winningConfingConfingView) throws BusinessException;



	public WinningConfing findById(String id);
}
