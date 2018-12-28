
package com.qt.air.cleaner.market.service.price;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.price.PriceModel;
import com.qt.air.cleaner.market.vo.price.PriceModelView;

public interface PriceModelService {

	/**
	 * 价格模型分页查询
	 * 
	 * @param priceModelView
	 * @param pageable
	 * @return
	 */
	public Page<PriceModel> findAllPriceModel(PriceModelView priceModelView, Pageable pageable);
	
	/**
	 * 价格模型信息更新或保存
	 * 
	 * @param priceModelView
	 * @throws BusinessException
	 */
	public void saveOrUpdate(PriceModelView priceModelView) throws BusinessException;
	
	/**
	 * 价格模型信息删除
	 * 
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 价格模型修改查询
	 * @param id
	 * @return
	 */
	public PriceModel findById(String id);

   /** 
	* @Title: findAll 
	* @Description: 查询所有价格模型名称
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/ 
	public List<PriceModel>  findAll();
	
}
