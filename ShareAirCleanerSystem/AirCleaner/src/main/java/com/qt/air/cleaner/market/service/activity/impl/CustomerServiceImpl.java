package com.qt.air.cleaner.market.service.activity.impl;

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
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.market.domain.activity.Customer;
import com.qt.air.cleaner.market.repository.activity.CustomerRepository;
import com.qt.air.cleaner.market.service.activity.CustomerService;
import com.qt.air.cleaner.market.vo.activity.CustomerView;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Resource
	CustomerRepository customerRepository;

	/**
	* 用户列表信息查询实现方法
	* 
	* @param customerView
	* @param pageable
	* @return
	*/
	@Override
	public Page<Customer> findAllCustomer(CustomerView customerView, Pageable pageable) {
		Specification<Customer> specification = new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String phoneNumber = customerView.getPhoneNumber();
				if (StringUtils.isNotBlank(phoneNumber)) { // 手机号码
					Predicate p1 = cb.like(root.get("phoneNumber"), phoneNumber + "%");
					conditions.add(p1);
				}
				String joinTime = customerView.getJoinTime();
				if (StringUtils.isNotBlank(joinTime)) { // 注册时间
					Predicate p2 = cb.like(root.get("joinTime"), "%" + joinTime + "%");
					conditions.add(p2);
				}
				String nickName = customerView.getNickName();
				if (StringUtils.isNotBlank(nickName)) { // 微信昵称
					Predicate p3 = cb.like(root.get("nickName"), "%" + nickName + "%");
					conditions.add(p3);
				}
				Predicate p4 = cb.equal(root.get("removed"), false);
				conditions.add(p4);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return customerRepository.findAll(specification, pageable);
	}

	@Override
	public Customer findById(String id) {
		return customerRepository.findOne(id);
	}
}

