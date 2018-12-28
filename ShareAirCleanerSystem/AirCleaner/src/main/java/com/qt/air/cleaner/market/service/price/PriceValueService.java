package com.qt.air.cleaner.market.service.price;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.price.PriceValue;
import com.qt.air.cleaner.market.vo.price.PriceValueView;

public interface PriceValueService {
	/**
	 * 指定模型价格分页查询
	 * 
	 * @param priceValueView
	 * @param pageable
	 * @return
	 */
	public Page<PriceValue> findAllPriceValue(PriceValueView priceValueView, Pageable pageable);
	
	/**
	 * 指定模型价格更新或保存
	 * 
	 * @param priceValueView
	 * @throws BusinessException
	 */
	public void saveOrUpdate(PriceValueView priceValueView) throws BusinessException;
	
	/**
	 * 指定模型价格删除
	 * 
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 指定模型价格修改查询
	 * @param id
	 * @return
	 */
	public PriceValue findById(String id);
}
