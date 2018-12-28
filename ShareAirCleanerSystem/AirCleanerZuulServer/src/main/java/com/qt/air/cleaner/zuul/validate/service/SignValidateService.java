package com.qt.air.cleaner.zuul.validate.service;

import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.zuul.dto.Head;

public interface SignValidateService {
	
	/**
	 * token验证
	 * 
	 * @param head
	 * @return
	 * @throws BusinessRuntimeException
	 */
	boolean tokenValidate(Head head) throws BusinessRuntimeException;
	
	/**
	 * 获取token
	 * 
	 * @return
	 */
	String getToken(String partner,String key);
}
