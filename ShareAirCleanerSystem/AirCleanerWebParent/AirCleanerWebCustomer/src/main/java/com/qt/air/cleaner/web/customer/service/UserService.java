package com.qt.air.cleaner.web.customer.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.web.customer.vo.Bound;
import com.qt.air.cleaner.web.customer.vo.PasswordInfo;
import com.qt.air.cleaner.web.customer.vo.SelfInfo;

@FeignClient(name = "user-service")
public interface UserService {
	
	/**
	 * 查询用户信息
	 * 
	 * @param wenxin
	 * @return
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public ResultInfo queryUser(@RequestBody Map<String,String> parame) throws BusinessRuntimeException;
	
	/**
	 * 用户绑定
	 * 
	 * @param bound
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value = "/bound", method = RequestMethod.POST)
	public ResultInfo bound(@RequestBody Bound bound)  throws BusinessRuntimeException;
	
	/**
	 * 更新用户信息
	 * 
	 * @param selfInfo
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value = "/updateSelfInfo", method = RequestMethod.POST)
	public ResultInfo updateSelfInfo(@RequestBody SelfInfo selfInfo)  throws BusinessRuntimeException;
	
	
	/**
	 * 更新手机号码
	 * 
	 * @param parames
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
	public ResultInfo updatePhoneNumber(@RequestBody Map<String,String> parames)  throws BusinessRuntimeException;
	
	/**
	 * 更新交易密码
	 * 
	 * @param selfInfo
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value = "/updateTradePwd", method = RequestMethod.POST)
	public ResultInfo updateTradePwd(@RequestBody PasswordInfo passwordInfo)  throws BusinessRuntimeException;
	
	/**
	 * 用户登录
	 * 
	 * @param wenxin
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultInfo loginOrBound(@RequestBody Map<String,String> parame) throws BusinessRuntimeException;
	
	/**
	 * 获取微信用户信息
	 * 
	 * @param parame
	 * @return
	 */
	@RequestMapping(value = "/wx/query", method = RequestMethod.POST)
	public ResultInfo obtainUserInfo(@RequestBody Map<String,Object> parame);
	
	/**
	 * 微信授权
	 * 
	 * @param userType
	 * @return
	 */
	@RequestMapping(value = "/wx/authorize", method = RequestMethod.POST)
	public ResultInfo authorize(@RequestParam("userType") String userType);
}
