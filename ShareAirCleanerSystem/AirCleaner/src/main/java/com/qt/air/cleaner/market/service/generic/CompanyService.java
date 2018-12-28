package com.qt.air.cleaner.market.service.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.Company;
import com.qt.air.cleaner.market.vo.generic.CompanyView;

/**
 * 公司信息维护
 * 
 * @author SongXueShuang
 *
 */
public interface CompanyService {
	/**
	 * 公司信息维护分页查询
	 * 
	 * @param socialCreditCode
	 * @param name
	 * @param address
	 * @param legalPerson
	 * @param phoneNumber
	 * @param email
	 * @param setupTime
	 * @param weixin
	 * @param alipay
	 * @return
	 */
	public Page<Company> findAllCompany(CompanyView companyView, Pageable pageable);
	/**
	 * 公司信息修改查询
	 * @param id
	 * @return
	 */
	public Company findById(String id);
	/** 
	* @Title: saveOrUpdate 
	* @Description: 公司信息新增或更新 
	* @param @param companyView    设定文件 
	* @return void    返回类型 
	* @throws 
	*/ 
	public void saveOrUpdate(CompanyView companyView);
	/** 
	* @Title: delete 
	* @Description: 公司信息删除
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
	List<Company> findAll(boolean removed);

}
