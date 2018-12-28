package com.qt.air.cleaner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.user.domain.Customer;
import com.qt.air.cleaner.user.domain.Saler;

@Repository
public interface SalerRepository extends JpaRepository<Saler, String> {
	Saler findByWeixinAndRemoved(String weixin,Boolean removed);
	Saler findByIdentificationNumberAndPhoneNumberAndRemoved(String identificationNumber,String phoneNumber,Boolean removed);
	void saveAndFlush(Customer customer);
	Saler findByWeixinAndPhoneNumberAndRemoved(String weixin,String phoneNumber,Boolean removed);
}
