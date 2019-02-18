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

import com.qt.air.cleaner.market.domain.account.Billing;
import com.qt.air.cleaner.market.repository.account.BillingRepository;
import com.qt.air.cleaner.market.service.account.BillingService;
import com.qt.air.cleaner.market.vo.account.BillingView;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {
	@Resource
	BillingRepository billingRepository;
	
	
	@Override
	public List<Billing> findAll(boolean removed) {
		return  billingRepository.findByRemoved(false);
	}
	
	/**
	 * 设备消费记录分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Billing> findAllBilling(BillingView billingView, Pageable pageable) {
		Specification<Billing> specification = new Specification<Billing>() {
			@Override
			public Predicate toPredicate(Root<Billing> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String billingId = billingView.getBillingId();
				if (StringUtils.isNotBlank(billingId)) { // 订单号
					Predicate p1 = cb.like(root.get("billingId"), "%" + StringUtils.trim(billingId) + "%");
					conditions.add(p1);
				}
				
				String machNo = billingView.getMachNo();
				if (StringUtils.isNotBlank(machNo)) { // 设备编号
					Predicate p2 = cb.like(root.get("machNo"), "%" + StringUtils.trim(machNo) + "%");
					conditions.add(p2);
				}
				Integer state = billingView.getState();
				if(state!=null) {//订单状态
					Predicate p3 = cb.equal(root.get("state"), state);
					conditions.add(p3);
				}
				
				String createTimeStr = billingView.getCreateTimeStr();
				if(StringUtils.isNoneBlank(createTimeStr)) {//消费时间
					whereTime(root, cb, conditions,createTimeStr);
				}
				Predicate p5 = cb.equal(root.get("removed"), false);
				conditions.add(p5);
				Predicate p6 = cb.isNotNull(root.get("transactionId"));
				conditions.add(p6);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}

		};
		return billingRepository.findAll(specification, pageable);
	
	
	}
	
	/**
	 * 根据日期查询
	 * 
	 * @param root
	 * @param cb
	 * @param conditions
	 * @param timeStr
	 */
	private void whereTime(Root<Billing>  root, CriteriaBuilder cb, List<Predicate> conditions,String timeStr) {
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

}
