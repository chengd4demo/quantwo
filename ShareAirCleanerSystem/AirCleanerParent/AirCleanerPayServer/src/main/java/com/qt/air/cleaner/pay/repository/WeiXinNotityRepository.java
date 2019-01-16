package com.qt.air.cleaner.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.pay.domain.WeiXinNotity;


@Repository
public interface WeiXinNotityRepository extends JpaRepository<WeiXinNotity, String> {

	WeiXinNotity findByOutTradeNo(String outTradeNo);

}
