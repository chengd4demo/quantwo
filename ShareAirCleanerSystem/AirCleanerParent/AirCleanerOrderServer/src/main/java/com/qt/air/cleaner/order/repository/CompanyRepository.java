package com.qt.air.cleaner.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.order.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>  {
	Company findByWeixinAndRemoved(String weixin,Boolean removed);
}
