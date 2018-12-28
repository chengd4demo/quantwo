package com.qt.air.cleaner.market.service.activity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.activity.PrizeConfig;
import com.qt.air.cleaner.market.vo.activity.PrizeConfigView;

public interface PrizeConfigService {

	/**
	 * 奖品设置分页模糊查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<PrizeConfig> findAllPrize(PrizeConfigView params, Pageable pageable);
	
	/**
	 * 奖品设置查询
	 * @param id
	 * @return
	 */
	PrizeConfig findById(String id);
	
	/**
	 * 奖品设置新增或修改
	 * 
	 * @param investorView
	 */
	void saveOrUpdate(PrizeConfigView prizeConfigView);
	
	/**
	 * 奖品设置信息删除
	 * 
	 * @param id
	 */
	void delete(String id);
	/**
	 * 奖品设置下拉列表
	 * 
	 * @param id
	 */
	List<PrizeConfig> findAll(boolean removed);
	
	/**
	 * 奖品设置状态更新
	 * 
	 * @param id
	 */
	void updateState(String id, String state);
	
}
