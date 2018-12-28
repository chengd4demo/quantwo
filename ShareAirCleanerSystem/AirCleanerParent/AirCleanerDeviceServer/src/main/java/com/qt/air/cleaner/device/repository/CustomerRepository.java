package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	Customer findByWeixinAndRemoved(String weixin, Boolean removed);
}
