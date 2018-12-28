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
import com.qt.air.cleaner.market.domain.generic.Saler;
import com.qt.air.cleaner.market.repository.account.AccountRepository;
import com.qt.air.cleaner.market.repository.generic.SalerRepository;
import com.qt.air.cleaner.market.service.generic.SalerService;
import com.qt.air.cleaner.market.vo.generic.SalerView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class SalerServiceImpl implements SalerService{
	@Resource
	SalerRepository salerRepository;
	@Resource
	AccountRepository accountRepository;
	

	@Override
	public List<Saler> findAll(boolean removed) {
		return  salerRepository.findByRemoved(false);
	}
	
	/**
	 * 促销员分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Saler> findAllSaler(SalerView salerView, Pageable pageable) {

		Specification<Saler> specification = new Specification<Saler>() {
			@Override
			public Predicate toPredicate(Root<Saler> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = salerView.getName();
				if (StringUtils.isNotBlank(name)) { // 促销员姓名
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}
				
				String phoneNumber = salerView.getPhoneNumber();
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
		return salerRepository.findAll(specification, pageable);
	
	}
	
	/**
	 * 促销员查询
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Saler findById(String id) {
		return salerRepository.findByIdAndRemoved(id, false);
	}
	
	/**
	 * 促销员新增或修改
	 * 
	 * @param salerView
	 */
	@Override
	public void saveOrUpdate(SalerView salerView) {
		if (salerView != null) {
			Saler saler = null;
			String id = salerView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isNoneBlank(id)) {
				saler = salerRepository.findByIdAndRemoved(id, false);
				if (saler != null) {
					BeanUtils.copyProperties(salerView, saler);
					saler.setLastOperator(principal.getUser().getUsername());
					saler.setLastOperateTime(nowDate);
				} else {
					throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(),ErrorCodeEnum.ES_1001.getMessage());
				}
			} else {
				saler = new Saler();
				BeanUtils.copyProperties(salerView, saler);
				saler.setCreateTime(nowDate);
				saler.setCreater(principal.getUser().getUsername());
				saler.setLastOperateTime(nowDate);
				saler.setLastOperator(principal.getUser().getUsername());
				Account account = new Account();
				account.setAccountType(Account.ACCOUNT_TYPE_COMPANY);
				account.setCreateTime(nowDate);
				account.setCreater(principal.getUser().getUsername());
				account =  accountRepository.saveAndFlush(account);
				saler.setAccount(account);
			}
			salerRepository.saveAndFlush(saler);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
		
	}
	
	/**
	 * 促销员信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		Saler saler = salerRepository.findByIdAndRemoved(id, false);
		salerRepository.deleteSaler(id);
		accountRepository.deleteAccount(saler.getAccount().getId());
		
	}

}
