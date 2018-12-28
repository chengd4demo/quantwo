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
import com.qt.air.cleaner.market.domain.generic.Investor;
import com.qt.air.cleaner.market.repository.account.AccountRepository;
import com.qt.air.cleaner.market.repository.generic.InvestorRepository;
import com.qt.air.cleaner.market.vo.generic.InvestorView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class InvestorServiceImpl implements com.qt.air.cleaner.market.service.generic.InvestorService {
	@Resource
	InvestorRepository investorRepository;
	@Resource
	AccountRepository accountRepository;
	
	@Override
	public List<Investor> findAll(boolean removed) {
		return investorRepository.findByRemoved(false);
	}
	
	/**
	 * 投资商分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Investor> findAllInvesttor(InvestorView investorView, Pageable pageable) {
		Specification<Investor> specification = new Specification<Investor>() {
			@Override
			public Predicate toPredicate(Root<Investor> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = investorView.getName();
				if (StringUtils.isNotBlank(name)) { // 菜单名称
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}
				
				String phoneNumber = investorView.getPhoneNumber();
				if (StringUtils.isNotBlank(phoneNumber)) { // 联系电话
					Predicate p2 = cb.like(root.get("phoneNumber"), "%" + StringUtils.trim(phoneNumber) + "%");
					conditions.add(p2);
				}
				Predicate p3 = cb.equal(root.get("removed"), false);
				conditions.add(p3);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return investorRepository.findAll(specification, pageable);
	}
	
	/**
	 * 投资商查询
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Investor findById(String id) {
		return investorRepository.findByIdAndRemoved(id, false);
	}
	
	/**
	 * 投资商新增或修改
	 * 
	 * @param investorView
	 */
	@Override
	public void saveOrUpdate(InvestorView investorView) {
		if (investorView != null) {
			Investor investor = null;
			String id = investorView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isNoneBlank(id)) {
				investor = investorRepository.findByIdAndRemoved(id, false);
				if (investor != null) {
					BeanUtils.copyProperties(investorView, investor);
					investor.setLastOperator(principal.getUser().getUsername());
					investor.setLastOperateTime(nowDate);
				} else {
					throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(),ErrorCodeEnum.ES_1001.getMessage());
				}
			} else {
				investor = new Investor();
				BeanUtils.copyProperties(investorView, investor);
				investor.setCreateTime(nowDate);
				investor.setCreater(principal.getUser().getUsername());
				investor.setLastOperateTime(nowDate);
				investor.setLastOperator(principal.getUser().getUsername());
				Account account = new Account();
				account.setAccountType(Account.ACCOUNT_TYPE_COMPANY);
				account.setCreateTime(nowDate);
				account.setCreater(principal.getUser().getUsername());
				account =  accountRepository.saveAndFlush(account);
				investor.setAccount(account);
			}
			investorRepository.saveAndFlush(investor);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
	}
	
	/**
	 * 投资商信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		Investor investor = investorRepository.findByIdAndRemoved(id, false);
		investorRepository.deleteInvestor(id);
		accountRepository.deleteAccount(investor.getAccount().getId());
	}

}
