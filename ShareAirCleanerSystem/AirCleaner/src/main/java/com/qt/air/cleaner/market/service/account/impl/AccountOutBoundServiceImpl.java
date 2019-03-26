package com.qt.air.cleaner.market.service.account.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.account.Account;
import com.qt.air.cleaner.market.domain.account.AccountOutBound;
import com.qt.air.cleaner.market.domain.account.OutBoundRejectReason;
import com.qt.air.cleaner.market.repository.account.AccountOutBoundRepository;
import com.qt.air.cleaner.market.repository.account.OutBoundRejectReasonRepository;
import com.qt.air.cleaner.market.service.account.AccountOutBoundService;
import com.qt.air.cleaner.market.vo.account.AccountOutBoundView;
import com.qt.air.cleaner.utils.CalculateUtils;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
public class AccountOutBoundServiceImpl implements AccountOutBoundService{

	@Resource
	AccountOutBoundRepository accountOutBoundRepository;	
	@Resource
	OutBoundRejectReasonRepository outBoundRejectReasonRepository;
	
	/**
	 * 提现记录分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<AccountOutBound> findAllAccountOutBound(AccountOutBoundView accountOutBoundView, Pageable pageable) {
		Specification<AccountOutBound> specification = new Specification<AccountOutBound>() {
			@Override
			public Predicate toPredicate(Root<AccountOutBound> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = accountOutBoundView.getName();
				if (StringUtils.isNotBlank(name)) { // 名称
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}
				Integer state = accountOutBoundView.getState();
				String timeEnd = accountOutBoundView.getTimeEnd();
				whereFiled(root, cb, conditions,timeEnd,state,accountOutBoundView.getMethod());	//动态条件查询
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return accountOutBoundRepository.findAll(specification, pageable);
	}
	
	/**
	 * 根据条件查询
	 * 
	 * @param root
	 * @param cb
	 * @param conditions
	 * @param timeStr
	 * @param method
	 */
	private void whereFiled(Root<AccountOutBound>  root, CriteriaBuilder cb, List<Predicate> conditions,String timeStr,Integer state, String method) {
		if (StringUtils.isNotBlank(timeStr)) {
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
			Predicate p4 = null;
			if (StringUtils.equals(Constants.ACCOUNT_OUT_BOUND_METHOD_RECORD, method)) {
				p4 = cb.between(root.get("lastOperateTime"), startDate, endDate);
			} else {
				p4 = cb.between(root.get("createTime"), startDate, endDate);
			}
			conditions.add(p4);
		}
		
		Expression<String> exp = root.<String>get("state");
		List<Integer> stateList = null;
		if(StringUtils.equals(Constants.ACCOUNT_OUT_BOUND_METHOD_RECORD, method)) {
			if (state != null) {
				stateList = new ArrayList<>(Arrays.asList(state));
				if(5 == state) stateList.add(-1);
			} else {
				stateList = Arrays.asList(-1,0,1,2,3);
			}
		} else {
			Predicate p3 = cb.equal(root.get("removed"), false);
			conditions.add(p3);
			if (state != null) {
				stateList = Arrays.asList(state);
			} else {
				stateList = Arrays.asList(0, 2, 3);
			}
		}
		conditions.add(exp.in(stateList));
	}
	
	/**
	 * 提现确认
	 * @param id
	 * 
	 * */ 
	@Override
	public void updateState(AccountOutBoundView accountOutBoundView) throws BusinessException{
		if(accountOutBoundView != null){
			AccountOutBound accountOutBound = null;
			String id = accountOutBoundView.getId();
			Integer state = accountOutBoundView.getState();		
			if (StringUtils.isNotBlank(id)) {
				accountOutBound = accountOutBoundRepository.findByIdAndRemoved(id, false);
				accountOutBound.setState(state);				
				String rejectReasonId =  accountOutBoundView.getRejectReasonId();
				if(StringUtils.isNotBlank(rejectReasonId)) {
					OutBoundRejectReason outBoundRejectReason = outBoundRejectReasonRepository.findOne(rejectReasonId);
					outBoundRejectReason.setRejectReason(accountOutBoundView.getRejectReason());
					accountOutBound.setOutBoundRejectReason(outBoundRejectReason);
				}
			    if(state == 3) {
			    	//审核拒绝,回滚冻结金额、可用余额、总额
					Account account = accountOutBound.getAccount();
					account.setFreezingAmount(CalculateUtils.sub(account.getFreezingAmount(), accountOutBound.getAmount()));
					account.setAvailableAmount(CalculateUtils.add(account.getAvailableAmount(), accountOutBound.getAmount()));
					account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(), accountOutBound.getAmount()));
					accountOutBound.setAccount(account);
			    }
				accountOutBoundRepository.saveAndFlush(accountOutBound);
			} else {
				throw new BusinessException(ErrorCodeEnum.ES_9011.getErrorCode(), ErrorCodeEnum.ES_9011.getMessage());
			}
		}
		
	}

	@Override
	public AccountOutBound findByIdRemoved(String id, boolean b) {
		return accountOutBoundRepository.findByIdAndRemoved(id, b);
	}

}
