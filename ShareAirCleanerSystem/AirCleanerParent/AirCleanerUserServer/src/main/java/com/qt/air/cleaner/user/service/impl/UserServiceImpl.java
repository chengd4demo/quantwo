package com.qt.air.cleaner.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.enums.ErrorCodeEnum;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.user.domain.Account;
import com.qt.air.cleaner.user.domain.Company;
import com.qt.air.cleaner.user.domain.Customer;
import com.qt.air.cleaner.user.domain.Investor;
import com.qt.air.cleaner.user.domain.Saler;
import com.qt.air.cleaner.user.domain.Trader;
import com.qt.air.cleaner.user.dto.Bound;
import com.qt.air.cleaner.user.dto.SelfInfo;
import com.qt.air.cleaner.user.repository.CompanyRepository;
import com.qt.air.cleaner.user.repository.CustomerRepository;
import com.qt.air.cleaner.user.repository.InvestorRepository;
import com.qt.air.cleaner.user.repository.SalerRepository;
import com.qt.air.cleaner.user.repository.TraderRepository;
import com.qt.air.cleaner.user.service.UserService;
import com.qt.air.cleaner.user.utils.JsonUtil;
import com.qt.air.cleaner.user.vo.UserInfo;
import com.qt.air.cleaner.user.vo.UserInfoResponse;
import com.qt.air.cleaner.user.vo.mp.OauthTokenResponse;
import com.qt.air.cleaner.user.wechat.core.WechatMpCore;

@RestController
@Transactional
public class UserServiceImpl implements UserService {
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private CustomerRepository customerRepository;
	@Resource
	private CompanyRepository companyRepository;
	@Resource
	private InvestorRepository investorRepository;
	@Resource
	private SalerRepository salerRepository;
	@Resource
	private TraderRepository traderRepository;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	@Value("${o2.wechat.subscription.app.secret}")
	public String appSecret;
	@Value("${o2.wechat.subscription.app.id}")
	public String appId;

	/**
	 * 查询用户信息
	 * 
	 * @param wenxin
	 * @return
	 */
	@Override
	public ResultInfo queryUser(@RequestBody Map<String, String> parame) throws BusinessRuntimeException {
		logger.info("execute method queryUser() param --> parame:{}", parame);
		String weixin = parame.get("openId");
		String userType = parame.get("userType");
		UserInfo userInfo = null;
		userInfo = this.findByWeixin(weixin, userType);
		if (userInfo == null) {
			return new ResultInfo(ErrorCodeEnum.ES_1002.getErrorCode(), ErrorCodeEnum.ES_1002.getMessage(), userInfo);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", userInfo);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param weixin
	 * @param userType
	 * @return
	 * @throws BusinessRuntimeException
	 */
	private UserInfo findByWeixin(String weixin, String userType) throws BusinessRuntimeException {
		UserInfo userInfo = null;
		if (StringUtils.isNotBlank(weixin) && StringUtils.isNotBlank(userType)) {

			if (StringUtils.equals("MERCHANT", userType)) {
				Company company = companyRepository.findByWeixinAndRemoved(weixin, false);
				if (company != null) {
					userInfo = new UserInfo();
					userInfo.setId(company.getId());
					userInfo.setIdentificationNumber(company.getSocialCreditCode());
					userInfo.setNickName(company.getLegalPerson());
					userInfo.setWeixin(company.getWeixin());
					userInfo.setUserType(Account.ACCOUNT_TYPE_COMPANY);
					userInfo.setPhoneNumber(company.getPhoneNumber());
					if (StringUtils.isNotBlank(company.getAlipay()))
						userInfo.setAlipay(true);
					return userInfo;
				}
				Investor investor = investorRepository.findByWeixinAndRemoved(weixin, false);
				if (investor != null) {
					userInfo = new UserInfo();
					userInfo.setId(investor.getId());
					userInfo.setIdentificationNumber(investor.getIdentificationNumber());
					userInfo.setNickName(investor.getLegalPerson());
					userInfo.setWeixin(investor.getWeixin());
					userInfo.setUserType(Account.ACCOUNT_TYPE_INVESTOR);
					userInfo.setPhoneNumber(investor.getPhoneNumber());
					if (StringUtils.isNotBlank(investor.getAlipay()))
						userInfo.setAlipay(true);
					return userInfo;
				}
				Trader trader = traderRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
				if (trader != null) {
					userInfo = new UserInfo();
					userInfo.setId(trader.getId());
					userInfo.setIdentificationNumber(trader.getSocialCreditCode());
					userInfo.setNickName(trader.getLegalPerson());
					userInfo.setWeixin(trader.getWeixin());
					userInfo.setUserType(Account.ACCOUNT_TYPE_TRADER);
					userInfo.setPhoneNumber(trader.getPhoneNumber());
					if (StringUtils.isNotBlank(trader.getAlipay()))
						userInfo.setAlipay(true);
					return userInfo;
				}
				Saler saler = salerRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
				if (saler != null) {
					userInfo = new UserInfo();
					userInfo.setId(saler.getId());
					userInfo.setIdentificationNumber(saler.getIdentificationNumber());
					userInfo.setNickName(saler.getName());
					userInfo.setWeixin(saler.getWeixin());
					userInfo.setUserType(Account.ACCOUNT_TYPE_SALER);
					userInfo.setPhoneNumber(saler.getPhoneNumber());
					if (StringUtils.isNotBlank(saler.getAlipay()))
						userInfo.setAlipay(true);
					return userInfo;
				}

			} else if (StringUtils.equals("CUSTOMER", userType)) {
				Customer customer = customerRepository.findByWeixinAndRemoved(weixin, false);
				if (customer != null) {
					userInfo = new UserInfo();
					BeanUtils.copyProperties(customer, userInfo);
					userInfo.setId(customer.getId());
					userInfo.setName(customer.getName());
					userInfo.setIdentificationNumber(customer.getIdentificationNumber());
					userInfo.setUserType(Account.ACCOUNT_TYPE_CUSTOMER);
					userInfo.setPhoneNumber(customer.getPhoneNumber());
					if (StringUtils.isNotBlank(customer.getAlipay()))
						userInfo.setAlipay(true);
					return userInfo;
				}
			}
		} else {
			throw new BusinessRuntimeException(ResultCode.R2001.code, ResultCode.R2001.info);
		}

		return userInfo;
	}

	/**
	 * 用户绑定
	 * 
	 * @param bound
	 * @return
	 */
	@Override
	public ResultInfo bound(@RequestBody Bound bound) throws BusinessRuntimeException {
		logger.info("execute method bound() param --> bound:{}", bound);
		/**
		 * 1.根据类型执行bound，或修改绑定
		 */
		if (bound != null) {
			/* ResultInfo resultInfo = null; */
			String openId = bound.getOpenId();
			String phoneNumber = bound.getPhoneNumber();
			String uniqueIdentifier = bound.getUniqueIdentifier();
			String identificationNumber = bound.getUniqueIdentifier();
			String socialCreditCode = bound.getUniqueIdentifier();
			Date nowDate = Calendar.getInstance().getTime();
			if (StringUtils.isNotBlank(bound.getUserType())) {
				if (StringUtils.equals("MERCHANT", bound.getUserType()) && StringUtils.isNotBlank(phoneNumber)
						&& StringUtils.isNotBlank(uniqueIdentifier)) {
					Company company = companyRepository.findBySocialCreditCodeAndPhoneNumberAndRemoved(uniqueIdentifier,
							phoneNumber, false);
					if (company != null) {
						company.setWeixin(openId);
						company.setLastOperateTime(nowDate);
						company.setLastOperator(company.getLegalPerson());
						companyRepository.saveAndFlush(company);
					}
					Investor investor = investorRepository.findByIdentificationNumberAndPhoneNumberAndRemoved(
							identificationNumber, phoneNumber, false);
					if (investor != null) {
						investor.setWeixin(openId);
						investor.setLastOperateTime(nowDate);
						investor.setLastOperator(investor.getLegalPerson());
						investorRepository.saveAndFlush(investor);
					}
					Trader trader = traderRepository.findBySocialCreditCodeAndPhoneNumberAndRemoved(socialCreditCode,
							phoneNumber, false);
					if (trader != null) {
						trader.setWeixin(openId);
						trader.setLastOperateTime(nowDate);
						trader.setLastOperator(trader.getLegalPerson());
						traderRepository.saveAndFlush(trader);
					}
					Saler saler = salerRepository.findByIdentificationNumberAndPhoneNumberAndRemoved(
							identificationNumber, phoneNumber, false);
					if (saler != null) {
						saler.setWeixin(openId);
						saler.setLastOperateTime(nowDate);
						saler.setLastOperator(saler.getCreater());
						salerRepository.saveAndFlush(saler);
					}
				} else if (StringUtils.equals("CUSTOMER", bound.getUserType()) && StringUtils.isNotBlank(phoneNumber)) {
					Customer customer = customerRepository.findByPhoneNumberAndRemoved(phoneNumber, false);
					if (StringUtils.isNoneBlank(openId) && StringUtils.isNotBlank(phoneNumber)) {
						customer.setWeixin(openId);
						customer.setLastOperateTime(nowDate);
						customer.setLastOperator(customer.getCreater());
						customerRepository.saveAndFlush(customer);
					}
				} else {
					return new ResultInfo(String.valueOf(ResultCode.R2001), ResultCode.R2001.info, null);
				}
			} else {
				return new ResultInfo(String.valueOf(ResultCode.R2001), ResultCode.R2001.info, null);
			}

		} else {
			throw new BusinessRuntimeException(ErrorCodeEnum.ES_1001.getErrorCode(),
					ErrorCodeEnum.ES_1001.getMessage());
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param selfInfo
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo updateSelfInfo(@RequestBody SelfInfo selfInfo) throws BusinessRuntimeException {
		logger.info("execute method updateSelfInfo() param --> selfInfo:{}", new Gson().toJson(selfInfo));
		if (selfInfo != null) {
			String weixin = selfInfo.getWeixin();
			String userType = selfInfo.getUserType();
			if (StringUtils.isNotEmpty(selfInfo.getWeixin())) {
				if (StringUtils.equals("CUSTOMER", userType)) {
					// 更新消费者信息
					Customer customer = customerRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
					if (customer != null) {
						customer.setSex(selfInfo.getSex());
						customer.setName(selfInfo.getName());
						customer.setIdentificationNumber(selfInfo.getIdentificationNumber());
						customer.setNickName(selfInfo.getNickName());
						customerRepository.saveAndFlush(customer);
					}
				} else {
					// 平台用户更新操作，查询公司、投资商、商家、促销员
					// 查询对象不等于null 更新相应字段
					Company company = companyRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
					if (company != null) {
						company.setLegalPerson(selfInfo.getNickName()); // 昵称
						company.setSocialCreditCode(selfInfo.getIdentificationNumber()); // 唯一识别号
						companyRepository.saveAndFlush(company);
					}
					Investor inverstor = investorRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
					if (inverstor != null) {
						inverstor.setLegalPerson(selfInfo.getNickName());
						inverstor.setIdentificationNumber(selfInfo.getIdentificationNumber());
						investorRepository.saveAndFlush(inverstor);
					}
					Trader trader = traderRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
					if (trader != null) {
						trader.setLegalPerson(selfInfo.getNickName());
						trader.setSocialCreditCode(selfInfo.getIdentificationNumber());
						traderRepository.saveAndFlush(trader);
					}
					Saler saler = salerRepository.findByWeixinAndRemoved(weixin, Boolean.FALSE);
					if (saler != null) {
						saler.setName(selfInfo.getNickName());
						saler.setIdentificationNumber(selfInfo.getIdentificationNumber());
						salerRepository.saveAndFlush(saler);
					}
				}
			}
		} else {
			throw new BusinessRuntimeException(ErrorCodeEnum.ES_1001.getErrorCode(),
					ErrorCodeEnum.ES_1001.getMessage());
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
	}

	/**
	 * 更新手机号码
	 * 
	 * @param selfInfo
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo updatePhoneNumber(@RequestBody Map<String, String> parames) throws BusinessRuntimeException {
		logger.info("execute method updatePhoneNumber() param --> parames:{}", parames);
		String phoneNumber = parames.get("phoneNumber");
		String weixin = parames.get("openId");
		String userType = parames.get("userType");
		String verificationCode = parames.get("verificationCode");
		boolean isOk = false;
		try {
			if (StringUtils.isNotBlank(phoneNumber) && StringUtils.isNotBlank(weixin)) {
				if (StringUtils.equals("CUSTOMER", userType)) {
					Customer customer = customerRepository.findByWeixinAndRemoved(weixin, false);
					if (customer != null) {
						customer.setPhoneNumber(phoneNumber);
						customerRepository.saveAndFlush(customer);
						isOk = true;
					}
				} else {
					Company company = companyRepository.findByWeixinAndRemoved(weixin, false);
					if (company != null) {
						company.setPhoneNumber(phoneNumber);
						companyRepository.saveAndFlush(company);
						isOk = true;
					}
					Investor investor = investorRepository.findByWeixinAndRemoved(weixin, false);
					if (investor != null) {
						investor.setPhoneNumber(phoneNumber);
						investorRepository.saveAndFlush(investor);
						isOk = true;
					}
					Trader trader = traderRepository.findByWeixinAndRemoved(weixin, false);
					if (trader != null) {
						trader.setPhoneNumber(phoneNumber);
						traderRepository.saveAndFlush(trader);
						isOk = true;
					}
					Saler saler = salerRepository.findByWeixinAndRemoved(weixin, false);
					if (saler != null) {
						saler.setPhoneNumber(phoneNumber);
						salerRepository.saveAndFlush(saler);
						isOk = true;
					}
				}
			} else {
				throw new BusinessRuntimeException(ResultCode.R2001.code, ResultCode.R2001.info);
			}
		} catch (BusinessRuntimeException e) {
			throw new BusinessRuntimeException(ResultCode.R5002.code, ResultCode.R5002.info);
		}
		if (isOk) {
			return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
		}
		return new ResultInfo(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage(), null);

	}

	/**
	 * 更新交易密码
	 * 
	 * @param selfInfo
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo updateTradePwd(@RequestBody Map<String, String> parames) throws BusinessRuntimeException {
		logger.info("execute method updateTradePwd() param --> parames:{}", parames);
		String phoneNumber = parames.get("phoneNumber");
		String userType = parames.get("userType");
		String traderPwd = parames.get("tradePwd");
		String oldTraderPwd = parames.get("oldTraderPwd");
		String verificationCode = parames.get("verificationCode");
		String weixin = parames.get("openId");

		try {
			if (StringUtils.isNotBlank(traderPwd) && StringUtils.isNotBlank(userType)
					&& StringUtils.isNotBlank(phoneNumber)) {
				if (StringUtils.isNotBlank(oldTraderPwd) && StringUtils.equals(oldTraderPwd, traderPwd)) {
					return new ResultInfo(ErrorCodeEnum.ES_1016.getErrorCode(), ErrorCodeEnum.ES_1016.getMessage(),
							null);
				}
				if (StringUtils.equals("CUSTOMER", userType)) {
					Customer customer = customerRepository.findByPhoneNumberAndRemoved(phoneNumber, false);
					if (customer != null) {
						customer.setAlipay(DigestUtils.md5Hex(traderPwd));
						customerRepository.saveAndFlush(customer);
					}
				} else {
					Company company = companyRepository.findByWeixinAndPhoneNumberAndRemoved(weixin, phoneNumber,
							false);
					boolean isOk = false;
					if (company != null) {
						company.setAlipay(DigestUtils.md5Hex(traderPwd));
						companyRepository.saveAndFlush(company);
						isOk = true;
					}
					Investor investor = investorRepository.findByWeixinAndPhoneNumberAndRemoved(weixin, phoneNumber,
							false);
					if (investor != null) {
						investor.setAlipay(DigestUtils.md5Hex(traderPwd));
						investorRepository.saveAndFlush(investor);
						isOk = true;
					}
					Trader trader = traderRepository.findByWeixinAndPhoneNumberAndRemoved(weixin, phoneNumber, false);
					if (trader != null) {
						trader.setAlipay(DigestUtils.md5Hex(traderPwd));

						traderRepository.saveAndFlush(trader);
						isOk = true;
					}
					Saler saler = salerRepository.findByWeixinAndPhoneNumberAndRemoved(weixin, phoneNumber, false);
					if (saler != null) {
						saler.setAlipay(DigestUtils.md5Hex(traderPwd));
						salerRepository.saveAndFlush(saler);
						isOk = true;
					}
					if (!isOk) {
						String error = String.format(ErrorCodeEnum.ES_1017.getMessage(),
								oldTraderPwd == null ? "设定" : "修改");
						return new ResultInfo(ErrorCodeEnum.ES_1017.getErrorCode(), error, null);
					}
				}
			} else {
				throw new BusinessRuntimeException(ResultCode.R2001.code, ResultCode.R2001.info);
			}
		} catch (BusinessRuntimeException e) {
			throw new BusinessRuntimeException(ResultCode.R5002.code, ResultCode.R5002.info);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", null);
	}

	/**
	 * 获取用户登陆信息
	 * 
	 * @param weixin
	 * @param userType
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo loginOrBound(@RequestBody Map<String, String> parame) throws BusinessRuntimeException {
		logger.info("execute method loginUser() param --> parame:{}", parame);
		String weixin = parame.get("openId");
		String phoneNumber = parame.get("phoneNumber");
		String identificationNumber = parame.get("identificationNumber");
		int sex = Integer.parseInt(parame.get("sex"));
		String nickName = parame.get("nickName");
		String headUrl = parame.get("headUrl");
		String address = parame.get("address");
		UserInfo userInfo = null;
		String userType = parame.get("userType");
		ResultInfo resultInfo = new ResultInfo();
		if (StringUtils.isNotBlank(phoneNumber)) {
			/** 输入验证码验证逻辑 */
			if (!this.checkedInvalidVerificationCode(phoneNumber)) { // 检查验证码是否超时
				resultInfo.setStatus(ErrorCodeEnum.ES_1015.getErrorCode());
				resultInfo.setDescription(ErrorCodeEnum.ES_1015.getMessage());
				logger.info("验证码是否超时");
				return resultInfo;
			}
			if (!this.checkedEqVerificationCode(parame)) { // 是否一致
				resultInfo.setStatus(ErrorCodeEnum.ES_1014.getErrorCode());
				resultInfo.setDescription(ErrorCodeEnum.ES_1014.getMessage());
				logger.info("验证码不一致");
				return resultInfo;
			}
			 
			if (StringUtils.equals("CUSTOMER", userType)) {
				Customer customer = customerRepository.findByWeixinAndRemoved(weixin, false);
				if (customer != null) {
					if (StringUtils.isEmpty(customer.getPhoneNumber())) {
						customer.setPhoneNumber(phoneNumber);
						customer = customerRepository.saveAndFlush(customer);
					}
					if (!StringUtils.equals(customer.getPhoneNumber(), phoneNumber)) {
						resultInfo.setStatus(ErrorCodeEnum.ES_1013.getErrorCode());
						resultInfo.setDescription(ErrorCodeEnum.ES_1013.getMessage());
					} else {
						resultInfo.setStatus(String.valueOf(ResultCode.SC_OK));
						resultInfo.setDescription("success");
						resultInfo.setData(this.findByWeixin(weixin, userType));
					}
				} else {
					Date nowDate = Calendar.getInstance().getTime();
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
					customer = new Customer();
					customer.setAddress(address);
					customer.setCreater(weixin);
					customer.setCreateTime(nowDate);
					customer.setWeixin(weixin);
					customer.setJoinTime(sf.format(nowDate));
					customer.setSex(sex);
					customer.setName(null);
					customer.setNickName(nickName);
					customer.setPhoneNumber(phoneNumber);
					customer.setHeaderUrl(headUrl);
					customer = customerRepository.saveAndFlush(customer);
					if (customer == null) {
						resultInfo.setStatus(ErrorCodeEnum.ES_1013.getErrorCode());
						resultInfo.setDescription(ErrorCodeEnum.ES_1013.getMessage());
					} else {
						userInfo = new UserInfo();
						BeanUtils.copyProperties(customer, userInfo);
						userInfo.setUserType(Account.ACCOUNT_TYPE_CUSTOMER);
						resultInfo.setStatus(String.valueOf(ResultCode.SC_OK));
						resultInfo.setDescription("success");
						resultInfo.setData(userInfo);
					}

				}
			} else {
				Saler saler = salerRepository.findByIdentificationNumberAndPhoneNumberAndRemoved(identificationNumber,
						phoneNumber, false);
				boolean isOk = false;
				if (null != saler) {
					if (!StringUtils.equals(saler.getWeixin(), weixin)) {
						saler.setWeixin(weixin);
						saler.setLastOperator(saler.getName());
						saler = salerRepository.saveAndFlush(saler);
					}
					isOk = true;
				}

				Trader trader = traderRepository.findBySocialCreditCodeAndPhoneNumberAndRemoved(identificationNumber,
						phoneNumber, false);
				if (trader != null) {
					if (!StringUtils.equals(trader.getWeixin(), weixin)) {
						trader.setWeixin(weixin);
						trader.setLastOperator(trader.getLegalPerson());
						trader = traderRepository.saveAndFlush(trader);
					}
					isOk = true;
				}

				Investor investor = investorRepository
						.findByIdentificationNumberAndPhoneNumberAndRemoved(identificationNumber, phoneNumber, false);
				if (investor != null) {
					if (!StringUtils.equals(investor.getWeixin(), weixin)) {
						investor.setWeixin(weixin);
						investor.setLegalPerson(investor.getLegalPerson());
						investor = investorRepository.saveAndFlush(investor);
					}
					isOk = true;
				}

				Company company = companyRepository.findBySocialCreditCodeAndPhoneNumberAndRemoved(identificationNumber,
						phoneNumber, false);
				if (company != null) {
					if (!StringUtils.equals(company.getWeixin(), weixin)) {
						company.setWeixin(weixin);
						company.setLastOperator(company.getLegalPerson());
						company = companyRepository.saveAndFlush(company);
					}
					isOk = true;
				}

				// 返回平台商户执行业务结果
				if (isOk) {
					resultInfo.setStatus(String.valueOf(ResultCode.SC_OK));
					resultInfo.setDescription("success");
					resultInfo.setData(this.findByWeixin(weixin, userType));
				} else {
					resultInfo.setStatus(ErrorCodeEnum.ES_1011.getErrorCode());
					resultInfo.setDescription(ErrorCodeEnum.ES_1011.getMessage());
					resultInfo.setData(null);
				}
			}
		} else {
			return new ResultInfo(String.valueOf(ResultCode.R2001), ResultCode.R2001.info, null);
		}
		return resultInfo;

	}

	/**
	 * 检查验证码是否失效
	 * 
	 * @param phoneNumber
	 * @return
	 */
	private boolean checkedInvalidVerificationCode(String phoneNumber) {
		String validVerificationCode = stringRedisTemplate.opsForValue().get(phoneNumber);
		if (StringUtils.isNoneBlank(validVerificationCode)) {
			return true;
		}
		return false;
	}

	/**
	 * 验证码检查
	 * 
	 * @param parame
	 * @return
	 */
	private boolean checkedEqVerificationCode(Map<String, String> parame) {
		String inVerificationCode = parame.get("inVerificationCode");
		String verificationCode  = parame.get("verificationCode");
		if (StringUtils.equals(inVerificationCode, verificationCode)) {
			return true;
		}
		return false;
	}
	
	@Override
	public ResultInfo obtainUserInfo(@RequestBody Map<String, Object> userInfoMap) {
		logger.info("execute method obtainUserInfo() param: --> {}", JsonUtil.format(userInfoMap));
		Map<String, Object> mapToken = new HashMap<String, Object>();
		mapToken.put("appid", appId);
        mapToken.put("secret", appSecret);
        mapToken.put("code", userInfoMap.get("code"));
        mapToken.put("grant_type", "authorization_code");
		try {
			//1.获取OauthToken、openId
			OauthTokenResponse oauthTokenResponse = WechatMpCore.obtainOauthAccessToken(mapToken);
			String accessToken = oauthTokenResponse.getAccess_token();
			String openId = oauthTokenResponse.getOpenid();
			logger.info("access_token->{}",accessToken);
			logger.info("access_token->{}",openId);
			logger.info("error code:->{}",oauthTokenResponse.getErrcode());
			if (StringUtils.isEmpty(accessToken)) {
				logger.info("网页授权获取ACCESS_TOKEN失败");
				return new ResultInfo(ErrorCodeEnum.ES_1018.getErrorCode(), ErrorCodeEnum.ES_1018.getMessage(), null);
			}
			//2.获取用户信息
			userInfoMap.put("openid", oauthTokenResponse.getOpenid());
			userInfoMap.put("access_token", accessToken);
			userInfoMap.put("lang", "zh_CN");
			UserInfoResponse response = WechatMpCore.obtainUserInfo(userInfoMap);
			logger.info("errorCode:->{}",response.getErrcode());
			logger.info("desc:->{}",response.getDesc());
			logger.info("============微信用户信息=============");
			logger.info("openId:->{}",response.getOpenid());
			logger.info("nickName:->{}",response.getNickname());
			logger.info("sex:->{}",response.getSex());
			logger.info("country:->{}",response.getCountry());
			logger.info("city:->{}",response.getCity());
			logger.info("headImageurl:->{}",response.getHeadimgurl());
			logger.info("获取用户信信息:{}",new Gson().toJson(response));
			return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("system error method obtainUserInfo,error:{}",e.getMessage());
		}
		return new ResultInfo(ErrorCodeEnum.ES_1018.getErrorCode(), ErrorCodeEnum.ES_1018.getMessage(), null);
	}

	@Override
	public ResultInfo authorize(@RequestParam("userType") String userType) {
		logger.info("execute method authorize() param: --> {}", userType);
		String redirectUrl = "http://zf2.quantwo.cn/app.html";
	    String url = WechatMpCore.generateWechatUrl(appId, "snsapi_userinfo", redirectUrl,
				"MERCHANT".equals(userType) ? "merchant" : "customere");
		logger.info(url);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",url);
	}

}