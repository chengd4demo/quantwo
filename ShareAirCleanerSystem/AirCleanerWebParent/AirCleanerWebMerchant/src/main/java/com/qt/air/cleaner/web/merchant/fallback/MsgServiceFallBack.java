package com.qt.air.cleaner.web.merchant.fallback;

import java.util.Map;

import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.merchant.service.MsgService;

public class MsgServiceFallBack implements MsgService {

	@Override
	public ResultInfo sendSms(String phoneNumber, String templateCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultInfo checkedValidVerificationCode(Map<String, String> parames) {
		// TODO Auto-generated method stub
		return null;
	}

}
