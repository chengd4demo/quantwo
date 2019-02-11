package com.qt.air.cleaner.market.service.account.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.market.domain.account.Account;
import com.qt.air.cleaner.market.domain.account.AccountInBound;
import com.qt.air.cleaner.market.repository.account.AccountInBoundRepository;
import com.qt.air.cleaner.market.repository.account.AccountRepository;
import com.qt.air.cleaner.market.service.account.AccountInBoundService;
import com.qt.air.cleaner.market.vo.account.AccountInBoundView;


@Service
@Transactional
public class AccountInBoundServiceImpl implements AccountInBoundService {
	@Resource
	AccountInBoundRepository accountInBoundRepository;
	@Resource
	AccountRepository accountRepository;
	
	
	@Override
	public List<AccountInBound> findAll(boolean removed) {
		return  accountInBoundRepository.findByRemoved(false);
	}
	
	/**
	 * 入账记录分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<AccountInBound> findAllAccountInBound(AccountInBoundView accountInBoundView, Pageable pageable) {
		Specification<AccountInBound> specification = new Specification<AccountInBound>() {			
			@Override
			public Predicate toPredicate(Root<AccountInBound> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = accountInBoundView.getName();
				if (StringUtils.isNotBlank(name)) { // 订单号
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}
				
				String inBoundTime = accountInBoundView.getInBoundTime();
				if (StringUtils.isNotBlank(inBoundTime)) { // 入账时间
					whereTime(root, cb, conditions,inBoundTime);
				}
				Integer state = accountInBoundView.getState();
				if(state!=null) {//订单状态
					Predicate p3 = cb.equal(root.get("state"), state);
					conditions.add(p3);
				}
				
				
				Predicate p4 = cb.equal(root.get("removed"), false);
				conditions.add(p4);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}

		};
		return accountInBoundRepository.findAll(specification, pageable);
	
	
	}
	
	/**
	 * 根据日期查询
	 * 
	 * @param root
	 * @param cb
	 * @param conditions
	 * @param timeStr
	 */
	private void whereTime(Root<AccountInBound>  root, CriteriaBuilder cb, List<Predicate> conditions,String timeStr) {
		// 处理时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		Date endDate;
		try {
			startDate = format.parse(timeStr);
		} catch (ParseException e) {
			startDate = new Date(946656000000L);// 2000 01 01
		}
		endDate = startDate;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.DATE, 1);
		endDate = calendar.getTime();
		calendar = null;
		Predicate p4 = cb.between(root.get("createTime"), startDate, endDate);
		conditions.add(p4);
	}

	/**
	 * 入账确认
	 * @param id
	 * 
	 * */ 
	@Override
	public void updateState(String id) {
		if(StringUtils.isNoneBlank(id)) {
			AccountInBound inBound = accountInBoundRepository.findByIdAndRemoved(id,Boolean.FALSE);
			if(inBound != null) {
				accountInBoundRepository.updateState(id);
				Account account = inBound.getAccount();
				account.setFreezingAmount(account.getFreezingAmount() - inBound.getAmount());
				account.setAvailableAmount(account.getAvailableAmount() + inBound.getAmount());
				accountRepository.saveAndFlush(account);
			}
		}
		
	}

}
