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
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.account.BillingRefund;
import com.qt.air.cleaner.market.domain.account.BillingRefundReason;
import com.qt.air.cleaner.market.repository.account.BillingRefundReasonRepository;
import com.qt.air.cleaner.market.repository.account.BillingRefundRepository;
import com.qt.air.cleaner.market.service.account.BillingRefundService;
import com.qt.air.cleaner.market.vo.account.BillingRefundView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class BillingRefundServiceImpl implements BillingRefundService {
	@Resource
	BillingRefundRepository billingRefundRepository;
	
	
	
	/**
	 * 退款记录分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<BillingRefund> findAllBillingRefund(BillingRefundView billingRefundView, Pageable pageable) {
		Specification<BillingRefund> specification = new Specification<BillingRefund>(){
			@Override
			public Predicate toPredicate(Root<BillingRefund> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions =new ArrayList<>();
				String refundStatus = billingRefundView.getRefundStatus();
				if(StringUtils.isNotBlank(refundStatus)) {//退款状态
					Predicate p1 = cb.equal(root.get("refundStatus"), refundStatus);
					conditions.add(p1);
				}
				String createTime = billingRefundView.getCreateTime();
				if(StringUtils.isNoneBlank(createTime)) {//退款时间
					whereTime(root, cb, conditions, createTime);
				}
				Predicate p2 = cb.equal(root.get("removed"), false);
				conditions.add(p2);
				Predicate[] p =new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return billingRefundRepository.findAll(specification,pageable);
	}
	
	/**
	 * 根据日期查询
	 * 
	 * @param root
	 * @param cb
	 * @param conditions
	 * @param timeStr
	 */
	private void whereTime(Root<BillingRefund>  root, CriteriaBuilder cb, List<Predicate> conditions,String timeStr) {
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
	 * 退款确认
	 * @param id
	 * 
	 * */ 
	@Override
	@Transactional
	public void updateState(BillingRefundView billingRefundView) throws BusinessException {
		if(billingRefundView != null){
			BillingRefund billingRefund = null;
			String id = billingRefundView.getId();
			String refundStatus = billingRefundView.getRefundStatus();
			if (StringUtils.isNotBlank(id)) {
				billingRefund = billingRefundRepository.findByIdAndRemoved(id, false);
				billingRefund.setRefundStatus(refundStatus);
				String rejectReasonId =  billingRefundView.getRejectReasonId();
				if(StringUtils.isNotBlank(rejectReasonId)) {
					BillingRefundReason billingRefundReason = billingRefundReasonRepository.findOne(rejectReasonId);
					billingRefundReason.setRejectReason(billingRefundView.getRejectReason());
					billingRefund.setBillingRefundReason(billingRefundReason);
				}
				billingRefundRepository.saveAndFlush(billingRefund);
			} else {
				throw new BusinessException(ErrorCodeEnum.ES_9011.getErrorCode(), ErrorCodeEnum.ES_9011.getMessage());
			}
		}
		
	}

	@Override
	public BillingRefund findByIdRemoved(String id, boolean b) {
		return billingRefundRepository.findByIdAndRemoved(id, b);
	}
	
	@Resource
	BillingRefundReasonRepository billingRefundReasonRepository;
	
}
