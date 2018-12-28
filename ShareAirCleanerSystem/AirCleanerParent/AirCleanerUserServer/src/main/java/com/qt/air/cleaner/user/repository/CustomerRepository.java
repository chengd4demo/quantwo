package com.qt.air.cleaner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.user.domain.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	Customer findByWeixinAndRemoved(String phoneNumber, Boolean removed);
	Customer findByPhoneNumberAndRemoved(String phoneNumber, Boolean removed);
	Customer findByPhoneNumberAndWeixin(String phoneNumber,String weixin);
}
