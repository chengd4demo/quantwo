package com.qt.air.cleaner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.user.domain.Agent;
import com.qt.air.cleaner.user.domain.Investor;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
	Agent findByWeixinAndRemoved(String weixin,Boolean removed);
	Agent findByIdentificationNumberAndPhoneNumberAndRemoved(String identificationNumber,String phoneNumber,Boolean removed);
	Agent findByWeixinAndPhoneNumberAndRemoved(String weixin,String phoneNumber,Boolean removed);
}
