package com.qt.air.cleaner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.user.domain.Trader;


@Repository
public interface TraderRepository extends JpaRepository<Trader,String>{
	Trader findByWeixinAndRemoved(String weixin,Boolean removed);
	Trader findBySocialCreditCodeAndPhoneNumberAndRemoved(String socialCreditCode,String phoneNumber,Boolean removed);
	Trader findByWeixinAndPhoneNumberAndRemoved(String weixin,String phoneNumber,Boolean removed);
}
