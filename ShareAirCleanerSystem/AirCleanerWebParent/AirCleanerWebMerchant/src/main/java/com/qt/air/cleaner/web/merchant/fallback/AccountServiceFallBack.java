package com.qt.air.cleaner.web.merchant.fallback;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.web.merchant.service.AccountService;

public class AccountServiceFallBack implements AccountService {

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

}
