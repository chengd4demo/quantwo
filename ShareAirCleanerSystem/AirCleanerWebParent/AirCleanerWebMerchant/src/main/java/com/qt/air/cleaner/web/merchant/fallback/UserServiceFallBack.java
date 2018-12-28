package com.qt.air.cleaner.web.merchant.fallback;

import java.util.Map;

import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.web.merchant.service.UserService;
import com.qt.air.cleaner.web.merchant.vo.Bound;
import com.qt.air.cleaner.web.merchant.vo.PasswordInfo;
import com.qt.air.cleaner.web.merchant.vo.PhoneInfo;
import com.qt.air.cleaner.web.merchant.vo.SelfInfo;

public class UserServiceFallBack implements UserService{

	@Override
	public ResultInfo queryUser(Map<String, String> parame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo bound(Bound bound) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo updateSelfInfo(SelfInfo selfInfo) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo updatePhoneNumber(PhoneInfo phoneInfo) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo updateTradePwd(PasswordInfo passwordInfo) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo loginOrBound(Map<String, String> parame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
