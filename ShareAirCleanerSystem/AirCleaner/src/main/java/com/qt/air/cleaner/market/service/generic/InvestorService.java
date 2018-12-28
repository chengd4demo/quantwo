package com.qt.air.cleaner.market.service.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.Investor;
import com.qt.air.cleaner.market.vo.generic.InvestorView;

public interface InvestorService {
	/**
	 * 查询投资商信息下拉列表
	 * 
	 * @param removed
	 * @return
	 */
	List<Investor> findAll(boolean removed);
	
	/**
	 * 投资商分页模糊查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<Investor> findAllInvesttor(InvestorView params, Pageable pageable);
	
	/**
	 * 投资商查询
	 * @param id
	 * @return
	 */
	Investor findById(String id);
	
	/**
	 * 投资商新增或修改
	 * 
	 * @param investorView
	 */
	void saveOrUpdate(InvestorView investorView);
	
	/**
	 * 投资商信息删除
	 * 
	 * @param id
	 */
	void delete(String id);
	
	
}
