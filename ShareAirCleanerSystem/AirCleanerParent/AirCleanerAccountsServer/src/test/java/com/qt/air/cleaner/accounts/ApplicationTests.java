package com.qt.air.cleaner.accounts;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.qt.air.cleaner.accounts.service.AccountService;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Autowired AccountService accountService;
	@Test
	public void contextLoads() throws BusinessRuntimeException {
		Map<String,String> parames = new HashMap<String,String>();
		parames.put("weixin", "oPmTlsiEtKhwo1fN9DiIdl8bq4nw");
		parames.put("amount", "1.00");
		parames.put("name", "肖晶");
		parames.put("userType", "TR");
		parames.put("identificationNumber", "510321198306090032");
		parames.put("password", "e10adc3949ba59abbe56e057f20f883e");
		ResultInfo result = accountService.applyForAccountOutbound(parames);
		System.out.println(new Gson().toJson(result));
	}

}
