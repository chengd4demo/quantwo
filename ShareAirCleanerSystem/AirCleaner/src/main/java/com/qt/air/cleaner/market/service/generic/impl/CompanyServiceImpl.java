package com.qt.air.cleaner.market.service.generic.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.account.Account;
import com.qt.air.cleaner.market.domain.generic.Company;
import com.qt.air.cleaner.market.repository.account.AccountRepository;
import com.qt.air.cleaner.market.repository.generic.CompanyRepository;
import com.qt.air.cleaner.market.service.generic.CompanyService;
import com.qt.air.cleaner.market.vo.generic.CompanyView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

/**
 * 公司信息维护业务层
 * 
 * @author SongXueShuang
 *
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
	@Resource
	CompanyRepository companyRepository;
	@Resource
	AccountRepository accountRepository;
	
	@Override
	public List<Company> findAll(boolean removed) {
		return companyRepository.findByRemoved(false);
	}
	/**
	* 公司信息查询实现方法
	* 
	* @param companyView
	* @param pageable
	* @return
	*/ 
	@Override
	public Page<Company> findAllCompany(CompanyView companyView, Pageable pageable) {
		Specification<Company> specification = new Specification<Company>() {
			@Override
			public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = companyView.getName();
				if (StringUtils.isNotBlank(name)) { // 公司名称
					Predicate p2 = cb.like(root.get("name"), "%" + name + "%");
					conditions.add(p2);
				}
				String phoneNumber = companyView.getPhoneNumber();
				if (StringUtils.isNotBlank(phoneNumber)) { // 联系电话
					Predicate p5 = cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
					conditions.add(p5);
				}
				String weixin = companyView.getWeixin();
				if (StringUtils.isNotBlank(weixin)) { // 微信
					Predicate p6 = cb.like(root.get("weixin"), "%" + weixin + "%");
					conditions.add(p6);
				}
				Predicate p9 = cb.equal(root.get("removed"), false);
				conditions.add(p9);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return companyRepository.findAll(specification, pageable);
	}
	
	/**
	 * 公司查询
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Company findById(String id) {
		return companyRepository.findByIdAndRemoved(id, false);
	}
	
	/**
	 * 公司新增或修改
	 * 
	 * @param companyView
	 */
	@Override
	public void saveOrUpdate(CompanyView companyView) {
		if (companyView != null) {
			Company company = null;
			String id = companyView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isNotBlank(id)) {
				company = companyRepository.findOne(id);
				BeanUtils.copyProperties(companyView, company);
				
			} else {
				company = new Company();
				BeanUtils.copyProperties(companyView, company);
				company.setCreateTime(nowDate);
				company.setCreater(principal.getUser().getUsername());
				company.setLastOperateTime(nowDate);
				company.setLastOperator(principal.getUser().getUsername());
				Account account = new Account();
				account.setAccountType(Account.ACCOUNT_TYPE_COMPANY);
				account.setCreateTime(nowDate);
				account.setCreater(principal.getUser().getUsername());
				account =  accountRepository.saveAndFlush(account);
				company.setAccount(account);
			}
			companyRepository.saveAndFlush(company);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
	}
	
	/**
	 *公司信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		Company company = companyRepository.findByIdAndRemoved(id, false);
		companyRepository.deleteCompany(id);
		accountRepository.deleteAccount(company.getAccount().getId());
	}


}
