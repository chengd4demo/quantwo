package com.qt.air.cleaner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.user.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,String>{
	Company findByWeixinAndRemoved(String weixin,Boolean removed);
	Company findBySocialCreditCodeAndPhoneNumberAndRemoved(String socialCreditCode,String phoneNumber,Boolean removed);
	Company findByWeixinAndPhoneNumberAndRemoved(String weixin,String phoneNumber,Boolean removed);
}
