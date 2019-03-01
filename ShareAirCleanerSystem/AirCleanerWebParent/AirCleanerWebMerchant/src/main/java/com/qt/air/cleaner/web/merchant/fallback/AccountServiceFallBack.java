package com.qt.air.cleaner.web.merchant.fallback;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.web.merchant.service.AccountService;

@Component
public class AccountServiceFallBack implements AccountService {
	Logger logger = LoggerFactory.getLogger(AccountServiceFallBack.class);
	@Override
	public ResultInfo queryAccountDetailByWeixin(String weixin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo queryAccountOutboundPage(RequestParame requestParame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo cleanAccountOutbound(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo applyForAccountOutbound(Map<String, String> parames) throws BusinessRuntimeException {
		logger.info("提现异常,方法名：applyForAccountOutbound()" );
		
		return null;
	}

}
