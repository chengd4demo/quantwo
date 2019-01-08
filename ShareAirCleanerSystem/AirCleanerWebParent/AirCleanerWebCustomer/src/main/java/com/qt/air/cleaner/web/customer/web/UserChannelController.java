package com.qt.air.cleaner.web.customer.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.customer.service.UserService;
import com.qt.air.cleaner.web.customer.vo.Bound;
import com.qt.air.cleaner.web.customer.vo.LoginInfo;
import com.qt.air.cleaner.web.customer.vo.PasswordInfo;
import com.qt.air.cleaner.web.customer.vo.PhoneInfo;
import com.qt.air.cleaner.web.customer.vo.SelfInfo;

@RestController
@RequestMapping("/user-channel")
public class UserChannelController {
	private static Logger logger = LoggerFactory.getLogger(UserChannelController.class);
	
	/**注入用户服务接口**/
	@Autowired
	private UserService userService;
	@PostMapping("/query/{openId}")
	public ResultInfo queryUserInfo(@PathVariable("openId") String openId) {
		logger.info("execute user-channel's method queryUserInfo()  start -> param:{}",openId);	
		try {
			if (StringUtils.isNotBlank(openId)) {
				Map<String,String> parames = new HashMap<String,String>();
				parames.put("openId", openId);
				parames.put("userType", "CUSTOMER");
				return userService.queryUser(parames);
			} else {
				return new ResultInfo(ResultCode.R2001.code,ResultCode.R2001.info,null);
			}
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	/**用户绑定**/
	@PostMapping("/bound")
	public ResultInfo boundInfo(@RequestBody Bound bound) {
		logger.info("execute user-channel's method bound()  start -> param:{}",new Gson().toJson(bound));
		try {
			bound.setOpenId(bound.getOpenId());
			bound.setPhoneNumber(bound.getPhoneNumber());
			bound.setSmsCode(bound.getSmsCode());
			bound.setUniqueIdentifier(bound.getUniqueIdentifier());
			bound.setOpenId(bound.getUserType());
			return userService.bound(bound);
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	/**更新用户信息**/
	@PostMapping("/updateSelfInfo")
	public ResultInfo updateSelfInfo(@RequestBody SelfInfo selfInfo) {
		logger.info("execute user-channel's method updateSelfInfo()  start -> param:{}",selfInfo.toString());
		try {			
			selfInfo.setUserType("CUSTOMER");
			return userService.updateSelfInfo(selfInfo);
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
		
	}
	
	/**更新手机号码**/
	@PostMapping("/updatePhone")
	public ResultInfo updatePhoneNumber(@RequestBody PhoneInfo phoneInfo) {
		logger.info("execute user-channel's method updatePhoneNumber()  start -> param:{}",phoneInfo.toString());
		try {
			Map<String,String> parames = new HashMap<String,String>();
			parames.put("openId", phoneInfo.getOpenId());
			parames.put("phoneNumber", phoneInfo.getPhoneNumber());
			parames.put("userType", phoneInfo.getUserType());			
			parames.put("verificationCode", phoneInfo.getVerificationCode());
			return userService.updatePhoneNumber(parames);
		} catch(Exception  e){
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}			
	
	/** 更新交易密码 **/
	@PostMapping("/updateTradePwd")
	public ResultInfo updateTradePwd(@RequestBody PasswordInfo passwordInfo) {
		logger.info("execute user-channel's method updateTradePwd()  start -> param:{}",passwordInfo.toString());
		try {
			passwordInfo.setOpenId(passwordInfo.getOpenId());
			passwordInfo.setPhoneNumber(passwordInfo.getPhoneNumber());
			passwordInfo.setTraderPwd(passwordInfo.getTraderPwd());
			passwordInfo.setUserType(passwordInfo.getUserType());
			return userService.updateTradePwd(passwordInfo);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}
	
	/** 用户登录 **/
	@PostMapping("/login")
	public ResultInfo loginOrBound(@RequestBody LoginInfo loginInfo) {
		logger.info("execute user-channel's method loginOrBound()  start -> param:{}", loginInfo.toString());
		try {
			Map<String,String> parames = new HashMap<String,String>();
			parames.put("nickName", loginInfo.getNickName());
			parames.put("headerUrl", loginInfo.getHeaderUrl());
			parames.put("openId", loginInfo.getWeixin());
			parames.put("inVerificationCode", loginInfo.getInVerificationCode());
			parames.put("verificationCode", loginInfo.getVerificationCode());
			parames.put("phoneNumber", loginInfo.getPhoneNumber());
			parames.put("sex", String.valueOf(loginInfo.getSex()));
			parames.put("userType", "CUSTOMER");
			return userService.loginOrBound(parames);			
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}

	}
	
	/**
	 * 微信授权
	 * 
	 * @param response
	 * @return
	 */
	@PostMapping("/wx/auth")
	public ResultInfo authorize(HttpServletResponse response) {
		logger.info("execute user-channel's method authorize()  start");
		try {
			userService.authorize(response, "MERCHANT");
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"sucess",null);
	}
	
	/**
	 * 获取微信用户信息
	 * 
	 * @param parame
	 * @return
	 */
	@PostMapping("/wx/query")
	public ResultInfo obtainUserInfo(Map<String, Object> parame) {
		logger.info("execute user-channel's method obtainUserInfo()  start -> param:{}",new Gson().toJson(parame));
		try {
			return userService.obtainUserInfo(parame);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}
}
