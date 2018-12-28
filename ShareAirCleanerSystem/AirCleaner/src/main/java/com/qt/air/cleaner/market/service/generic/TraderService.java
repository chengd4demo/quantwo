package com.qt.air.cleaner.market.service.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.Trader;
import com.qt.air.cleaner.market.vo.generic.TraderView;
/**
 * 商家信息维护
 * 
 * @author SongXueShuang
 *
 */
public interface TraderService {
	/**
	 * 商家信息维护分页查询
	 * 
	 * @param traderView
	 * @param pageable
	 * @return
	 */
	public Page<Trader> findAllTrader(TraderView traderView,Pageable pageable);
	/**
	 * 商家信息修改查询
	 * @param id
	 * @return
	 */
	public Trader findById(String id);
	/** 
	* @Title: saveOrUpdate 
	* @Description: 商家信息新增或更新
	* @param traderView    设定文件 
	* @return void    返回类型 
	* @throws 
	*/ 
	public void saveOrUpdate(TraderView traderView);
	/** 
	* @Title: delete 
	* @Description: 商家信息删除 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	*/ 
	public void delete(String id);
	/** 
	* @Title: findAll  
	* @param @param b
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/ 
	List<Trader> findAll(boolean removed);
}
