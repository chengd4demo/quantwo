package com.qt.air.cleaner.market.service.activity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.activity.PrizeItemConfig;
import com.qt.air.cleaner.market.vo.activity.PrizeItemConfigView;

public interface PrizeItemConfigService {
	/**
	 * 奖项设置分页模糊查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<PrizeItemConfig> findAllPrizeItem(PrizeItemConfigView params, Pageable pageable);
	
	/**
	 * 奖项设置查询
	 * @param id
	 * @return
	 */
	PrizeItemConfig findById(String id);
	
	/**
	 * 奖项设置新增或修改
	 * 
	 * @param investorView
	 */
	void saveOrUpdate(PrizeItemConfigView prizeItemConfigView);
	
	/**
	 * 奖项设置信息删除
	 * 
	 * @param id
	 */
	void delete(String id);	
	/**
	 * 奖项设置下拉列表
	 * 
	 * @param removed
	 * @return
	 */
	List<PrizeItemConfig> findAll(boolean removed);
}
