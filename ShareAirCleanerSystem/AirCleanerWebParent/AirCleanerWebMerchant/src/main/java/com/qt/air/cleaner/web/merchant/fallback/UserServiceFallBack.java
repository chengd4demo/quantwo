package com.qt.air.cleaner.web.merchant.fallback;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;

import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.web.merchant.service.UserService;
import com.qt.air.cleaner.web.merchant.vo.Bound;
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
	public ResultInfo updatePhoneNumber(@RequestBody Map<String,String> parames) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo loginOrBound(Map<String, String> parame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo updateTradePwd(Map<String, String> parames) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo obtainUserInfo(Map<String, Object> parame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo authorize(String userType) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
