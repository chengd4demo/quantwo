package com.qt.air.cleaner.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.order.domain.Agent;


@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
	Agent findByWeixinAndRemoved(String weixin,Boolean removed);
	Agent findByIdentificationNumberAndPhoneNumberAndRemoved(String identificationNumber,String phoneNumber,Boolean removed);
	Agent findByWeixinAndPhoneNumberAndRemoved(String weixin,String phoneNumber,Boolean removed);
	Agent findByWeixin(String weixin);
}
