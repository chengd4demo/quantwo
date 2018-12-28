package com.qt.air.cleaner.market.service.price;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.price.PriceSystem;
import com.qt.air.cleaner.market.vo.price.PriceSystemView;

public interface PriceSystemService {

	/**
	 * 指定价格体系管理分页查询
	 * 
	 * @param priceValueView
	 * @param pageable
	 * @return
	 */
	public Page<PriceSystem> findAllPriceSystem(PriceSystemView priceSystemView, Pageable pageable);
	
	/**
	 * 指定格体系管理更新或保存
	 * 
	 * @param priceValueView
	 * @throws BusinessException
	 */
	public void saveOrUpdate(PriceSystemView priceSystemView) throws BusinessException;
	
	/**
	 * 指定格体系管理删除
	 * 
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 指定格体系管理修改查询
	 * @param id
	 * @return
	 */
	public PriceSystem findById(String id);

	/** 
	* @Title: findAll 
	* @param @param b
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/ 
	List<PriceSystem> findAll(boolean removed);
	
	/**
	 * 价格体系管理设置状态更新
	 * 
	 * @param id
	 */
	public void updateState(String id, Integer state);
}
