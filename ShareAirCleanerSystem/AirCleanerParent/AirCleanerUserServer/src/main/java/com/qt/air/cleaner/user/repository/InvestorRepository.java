package com.qt.air.cleaner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.user.domain.Investor;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {
	Investor findByWeixinAndRemoved(String weixin,Boolean removed);
	Investor findByIdentificationNumberAndPhoneNumberAndRemoved(String identificationNumber,String phoneNumber,Boolean removed);
	Investor findByWeixinAndPhoneNumberAndRemoved(String weixin,String phoneNumber,Boolean removed);
}
