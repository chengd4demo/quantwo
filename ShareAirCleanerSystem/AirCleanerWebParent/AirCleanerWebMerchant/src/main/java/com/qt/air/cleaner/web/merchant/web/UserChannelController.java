package com.qt.air.cleaner.web.merchant.web;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.merchant.service.UserService;
import com.qt.air.cleaner.web.merchant.vo.Bound;
import com.qt.air.cleaner.web.merchant.vo.LoginInfo;
import com.qt.air.cleaner.web.merchant.vo.PasswordInfo;
import com.qt.air.cleaner.web.merchant.vo.PhoneInfo;
import com.qt.air.cleaner.web.merchant.vo.SelfInfo;


/**
 * @ClassName: DeviceChannelController
 * @Desc: 用户客户端Api
 * @Author: Jansan.Ma
 * @Date: 2018年11月20日
 */
/**
 * @author Administrator
 *
 */
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
				parames.put("userType", "MERCHANT");
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
		logger.info("execute user-channel's method bound()  start -> param:{}",bound.toString());
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
			selfInfo.setName(selfInfo.getName());
			selfInfo.setSex(selfInfo.getSex());
			selfInfo.setWeixin(selfInfo.getWeixin());
			selfInfo.setNickName(selfInfo.getNickName());
			selfInfo.setIdentificationNumber(selfInfo.getIdentificationNumber());
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
			Map<String,String> parames = new HashMap<>();
			parames.put("tradePwd", passwordInfo.getTradePwd());
			parames.put("phoneNumber", passwordInfo.getPhoneNumber());
			parames.put("oldTradePwd", passwordInfo.getOldTradePwd());
			parames.put("verificationCode", passwordInfo.getVerificationCode());
			parames.put("openId", passwordInfo.getOpenId());
			parames.put("userType","MERCHANT");
			return userService.updateTradePwd(parames);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}
	
	/** 用户登录 **/
	@PostMapping("/login")
	public ResultInfo loginOrBound(@RequestBody LoginInfo loginInfo) {
		logger.info("execute user-channel's method loginOrBound()  start -> param:{}", loginInfo);
		try {
			Map<String,String> parames = new HashMap<String,String>();
			parames.put("name", loginInfo.getName());
			parames.put("nickName", loginInfo.getNickName());
			parames.put("headerUrl", loginInfo.getHeaderUrl());
			parames.put("identificationNumber", loginInfo.getIdentificationNumber());
			parames.put("openId", loginInfo.getWeixin());
			parames.put("verificationCode", loginInfo.getVerificationCode());
			parames.put("amount", loginInfo.getAmount());
			parames.put("phoneNumber", loginInfo.getPhoneNumber());
			parames.put("sex", String.valueOf(loginInfo.getSex()));
			parames.put("userType", "MERCHANT");
			return userService.loginOrBound(parames);			
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}
	
	/**
	 * 微信授权
	 * 
	 * @return
	 */
	@PostMapping("/wx/auth")
	public ResultInfo authorize() {
		logger.info("execute user-channel's method authorize()  start");
		try {
			return userService.authorize("MERCHANT");
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}
	
	/**
	 * 获取微信用户信息
	 * 
	 * @param parame
	 * @return
	 */
	@PostMapping("/wx/query")
	public ResultInfo obtainUserInfo(@RequestBody Map<String, Object> parame) {
		logger.info("execute user-channel's method obtainUserInfo()  start -> param:{}",new Gson().toJson(parame));
		try {
			return userService.obtainUserInfo(parame);
		} catch (Exception e) {
			logger.error("system error: {}", e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code), e.getMessage(), null);
		}
	}
}
