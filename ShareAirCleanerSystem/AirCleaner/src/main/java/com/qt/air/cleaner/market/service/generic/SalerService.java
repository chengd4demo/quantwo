package com.qt.air.cleaner.market.service.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.Saler;
import com.qt.air.cleaner.market.vo.generic.SalerView;

public interface SalerService {
	/**
	 * 查询投资商信息下拉列表
	 * 
	 * @param removed
	 * @return
	 */
	List<Saler> findAll(boolean removed);
	
	/**
	 * 投资商分页模糊查询
	 * 
	 * @param salerView
	 * @param pageable
	 * @return
	 */
	Page<Saler> findAllSaler(SalerView salerView, Pageable pageable);
	
	/**
	 * 投资商查询
	 * @param id
	 * @return
	 */
	Saler findById(String id);
	
	/**
	 * 投资商新增或修改
	 * 
	 * @param salerView
	 */
	void saveOrUpdate(SalerView salerView);
	
	/**
	 * 投资商信息删除
	 * 
	 * @param id
	 */
	void delete(String id);
	
	
}
