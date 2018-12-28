package com.qt.air.cleaner.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.accounts.domain.Company;

@Repository
public interface CompanyService extends JpaRepository<Company, String> {
	Company findByWeixin(String weixin);
}
