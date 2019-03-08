package com.qt.air.cleaner.web.customer.fallback;

import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.customer.service.MsgService;

public class MsgServiceFallBack implements MsgService{

	@Override
	public ResultInfo sendSms(String phoneNumber,String templateCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
