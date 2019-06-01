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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.account.Account;
import com.qt.air.cleaner.market.domain.generic.Trader;
import com.qt.air.cleaner.market.repository.account.AccountRepository;
import com.qt.air.cleaner.market.repository.generic.TraderRepository;
import com.qt.air.cleaner.market.vo.generic.TraderView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
/**
 * 商家信息维护业务层
 * 
 * @author SongXueShuang
 *
 */

@Service
@Transactional
public class TraderServiceImpl implements com.qt.air.cleaner.market.service.generic.TraderService{
	
	@Resource
	TraderRepository traderRepository;
	@Resource
	AccountRepository accountRepository;
	
	@Override
	public List<Trader> findAll(boolean removed) {
		return traderRepository.findByRemoved(false);
	} 
	
   /**
	* 商家信息维护查询实现方法
	* 
	* @param traderView
	* @param pageable
	* @return
	*/ 
	@Override
	public Page<Trader> findAllTrader(TraderView traderView, Pageable pageable) {
		Specification<Trader> specification = new Specification<Trader>() {
			@Override
			public Predicate toPredicate(Root<Trader> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = traderView.getName();
				if (StringUtils.isNotBlank(name)) { // 商户名称
					Predicate p2 = cb.like(root.get("name"), "%" + name + "%");
					conditions.add(p2);
				}
				String phoneNumber = traderView.getPhoneNumber();
				if (StringUtils.isNotBlank(phoneNumber)) { // 联系电话
					Predicate p5 = cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
					conditions.add(p5);
				}
				Predicate p9 = cb.equal(root.get("removed"), false);
				conditions.add(p9);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return traderRepository.findAll(specification, pageable);
	}
	/**
	 * 商家信息修改查询
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Trader findById(String id) {
		return traderRepository.findOne(id);
	}
	
	@Override
	public void saveOrUpdate(TraderView traderView) {
		if (traderView != null) {
			Trader trader = null;
			String id = traderView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isNotBlank(id)) {
				trader = traderRepository.findOne(id);
				BeanUtils.copyProperties(traderView, trader);
				
			} else {
				trader = new Trader();
				BeanUtils.copyProperties(traderView, trader);
				trader.setCreateTime(nowDate);
				trader.setCreater(principal.getUser().getUsername());
				trader.setLastOperateTime(nowDate);
				trader.setLastOperator(principal.getUser().getUsername());
				Account account = new Account();
				account.setAccountType(Account.ACCOUNT_TYPE_TRADER);
				account.setCreateTime(nowDate);
				account.setCreater(principal.getUser().getUsername());
				account =  accountRepository.saveAndFlush(account);
				trader.setAccount(account);
			}
			traderRepository.saveAndFlush(trader);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
	}
	
	/**
	 *商家信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		Trader trader = traderRepository.findByIdAndRemoved(id, false);
		traderRepository.deleteTrader(id);
		accountRepository.deleteAccount(trader.getAccount().getId());
	}


}

	

