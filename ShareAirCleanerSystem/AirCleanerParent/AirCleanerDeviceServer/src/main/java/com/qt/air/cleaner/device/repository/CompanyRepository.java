package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,String>{
	Company findByWeixin(String weixin);
}
