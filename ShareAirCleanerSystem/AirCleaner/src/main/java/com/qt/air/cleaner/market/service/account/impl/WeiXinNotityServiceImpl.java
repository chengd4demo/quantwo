package com.qt.air.cleaner.market.service.account.impl;

import java.util.ArrayList;
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
import com.qt.air.cleaner.market.domain.account.WeiXinNotity;
import com.qt.air.cleaner.market.repository.account.WeiXinNotityRepository;
import com.qt.air.cleaner.market.service.account.WeiXinNotityService;
import com.qt.air.cleaner.market.vo.account.WeiXinNotityView;

@Service
public class WeiXinNotityServiceImpl implements WeiXinNotityService{

	@Resource
	WeiXinNotityRepository weiXinNotityRepository;
	
	@Override
	public Page<WeiXinNotity> findAllWeiXinNotity(WeiXinNotityView weiXinNotityView, Pageable pageable) {
		Specification<WeiXinNotity> specification = new Specification<WeiXinNotity>() {
			@Override
			public Predicate toPredicate(Root<WeiXinNotity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String outTradeNo = weiXinNotityView.getOutTradeNo();
				if (StringUtils.isNotBlank(outTradeNo)) { // 订单编号
					Predicate p1 = cb.like(root.get("outTradeNo"), "%" + outTradeNo + "%");
					conditions.add(p1);
				}
				String transactionId = weiXinNotityView.getTransactionId();
				if (StringUtils.isNotBlank(transactionId)) { // 交易流水号
					Predicate p2 = cb.like(root.get("transactionId"), "%" + StringUtils.trim(transactionId) + "%");
					conditions.add(p2);
				}
				String deviceInfo = weiXinNotityView.getDeviceInfo();
				if (StringUtils.isNotBlank(deviceInfo)) { // 设备编码
					Predicate p3 = cb.like(root.get("deviceInfo"), "%" + StringUtils.trim(deviceInfo) + "%");
					conditions.add(p3);
				}
				String timeEnd = weiXinNotityView.getTimeEnd();
				if (StringUtils.isNotBlank(timeEnd)) { // 支付完成时间
					Predicate p4 = cb.like(root.get("timeEnd"), StringUtils.trim(timeEnd.replaceAll("-", "")) + "%");
					conditions.add(p4);
				}
				Predicate p5 = cb.equal(root.get("removed"), false);
				conditions.add(p5);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return weiXinNotityRepository.findAll(specification, pageable);
	}
	
	
}
