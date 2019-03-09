package com.qt.air.cleaner.web.merchant.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.enums.ErrorCodeEnum;
import com.qt.air.cleaner.base.enums.TemplateCodeEnum;
import com.qt.air.cleaner.web.merchant.service.MsgService;
import com.qt.air.cleaner.web.merchant.service.TokenService;


@RestController
@RequestMapping("/common-channel")
public class CommonController {
	private Logger logger = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MsgService msgService;
	@GetMapping("/token")
	public ResultInfo queryToken(HttpServletResponse  response,@RequestParam("partner") String partner) {
		logger.info("execute common-channel's method queryToken()  start -> param{}",partner);	
		try {
			String token = tokenService.getToken(partner);
			if(token != null) {
				response.setHeader("head", token);
			}
			return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success");
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	@GetMapping("/sendSms/{phoneNumber}")
	public ResultInfo sendSms(@PathVariable("phoneNumber") String phoneNumber,@RequestParam("smsType") String smsType) {
		logger.info("execute common-channel's method sendSms()  start -> param:",phoneNumber);
		ResultInfo resultInfo = new ResultInfo(ErrorCodeEnum.ES_1011.getErrorCode(),ErrorCodeEnum.ES_1011.getMessage(),null);
		try {
			if(StringUtils.isNotBlank(phoneNumber) && StringUtils.isNotBlank(smsType)) {
				String templateCode = TemplateCodeEnum.TEMP_DEFAULT.getTemplateCode();
				//获取短信模板类型
				if (StringUtils.equals(TemplateCodeEnum.TEMP_SIGN.getType(), smsType)) {
					templateCode = TemplateCodeEnum.TEMP_SIGN.getTemplateCode();
				} else if (StringUtils.equals(TemplateCodeEnum.TEMP_CHANGE_PHONE.getType(), smsType)) {
					templateCode = TemplateCodeEnum.TEMP_CHANGE_PHONE.getTemplateCode();
				} else if (StringUtils.equals(TemplateCodeEnum.TEMP_CHANGE_APPLY.getType(), smsType)) {
					templateCode = TemplateCodeEnum.TEMP_CHANGE_APPLY.getTemplateCode();
				}
				resultInfo = msgService.sendSms(phoneNumber,templateCode);
			} else {
				resultInfo.setStatus(ResultCode.R2001.code);
				resultInfo.setDescription(ResultCode.R2001.info);
				resultInfo.setData(null);
				return resultInfo;
			}
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
		return resultInfo;
	}
	
	@PostMapping("checked/validVerificationCode")
	public ResultInfo checkedValidVerificationCode(@RequestBody Map<String, String> parames) {
		logger.info("execute common-channel's method checkedValidVerificationCode()  start -> param:",new Gson().toJson(parames));
		try {
			return msgService.checkedValidVerificationCode(parames);
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
}
