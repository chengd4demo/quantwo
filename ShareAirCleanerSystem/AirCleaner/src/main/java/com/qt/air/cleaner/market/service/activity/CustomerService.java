package com.qt.air.cleaner.market.service.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.activity.Customer;
import com.qt.air.cleaner.market.vo.activity.CustomerView;

public interface CustomerService {

	/**
	 * 用户列表分页查询
	 * 
	 * @param priceValueView
	 * @param pageable
	 * @return
	 */
	public Page<Customer> findAllCustomer(CustomerView customerView, Pageable pageable);
	
	
	public Customer findById(String id);
}
