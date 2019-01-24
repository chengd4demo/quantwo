package com.qt.air.cleaner.web.customer.fallback;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.web.customer.service.PayService;

public class PayServiceFallBack implements PayService {

	
	@Override
	public ResultInfo createBilling(RequestParame requestParame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo completeBilling(RequestParame requestParame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo payAuth(RequestParame requestParame) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo billingNotify(String strXML) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo weiXinMsg(String type, String billingNumber) throws BusinessRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}



}
