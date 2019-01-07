package com.qt.air.cleaner.user.service.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.user.Application;
import com.qt.air.cleaner.user.dto.Bound;
import com.qt.air.cleaner.user.dto.SelfInfo;
import com.qt.air.cleaner.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class UserServiceImplTest implements UserService{

	@Autowired UserService userService;
	

	@Test
	public void test() {
		Map<String,String> parames = new HashMap<String,String>(){{
			put("phoneNumber","13926113495");
			put("userType","MERCHANT");
			put("openId","oPmTlsjbuV49eGGacBxGK0kJjmFA");
			put("tradePwd","123456");
			put("tradePwd","654321");
		}};
		try {
			ResultInfo resunt = this.updateTradePwd(parames);
			new Gson().toJson(resunt);
		} catch (BusinessRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	public ResultInfo updatePhoneNumber(Map<String, String> parames) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo updateTradePwd(Map<String, String> parames) throws BusinessRuntimeException {
		return userService.updateTradePwd(parames);
	}

	@Override
	public ResultInfo loginOrBound(Map<String, String> parame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo obtainUserInfo(Map<String, Object> parame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo authorize(HttpServletResponse response, String userType) {
		// TODO Auto-generated method stub
		return null;
	}

}
