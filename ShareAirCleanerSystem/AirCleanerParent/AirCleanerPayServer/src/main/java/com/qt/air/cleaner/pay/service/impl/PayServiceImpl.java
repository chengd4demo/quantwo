package com.qt.air.cleaner.pay.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.enums.ErrorCodeEnum;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.pay.domain.Billing;
import com.qt.air.cleaner.pay.domain.Device;
import com.qt.air.cleaner.pay.domain.PriceValue;
import com.qt.air.cleaner.pay.domain.Token;
import com.qt.air.cleaner.pay.domain.WeiXinNotity;
import com.qt.air.cleaner.pay.domain.WeixinOauth2Token;
import com.qt.air.cleaner.pay.repository.BillingRepository;
import com.qt.air.cleaner.pay.repository.WeiXinNotityRepository;
import com.qt.air.cleaner.pay.service.PayService;
import com.qt.air.cleaner.pay.utils.WeiXinCommonUtil;

@RestController
@Transactional
public class PayServiceImpl implements PayService {
	private final static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Resource
	private BillingRepository billingRepository;
	@Resource
	private WeiXinNotityRepository weiXinNotityRepository;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Value("${o2.wechat.subscription.api.secret}")
	public String apiSecret;
	@Value("${o2.wechat.subscription.app.id}")
	public String appId;
	@Value("${o2.wechat.subscription.app.secret}")
	public String appSecret;
	@Value("${o2.system.domain}")
	public String systeDomain;
	@Value("${o2.wechat.subscription.mah.id}")
	public String mahId;
	@Value("${o2.wechat.cert.stream.path}")
	public String certStreamPath;
	@Value("${o2.wechat.subscription.notify.url}")
	public String notifyUrl;
	@Value("${o2.wechat.subscription.body.description}")
	public String body;
	
	@Value("${o2.weixin.cache.name}")
	public String WEIXIN_CACHE_NAME;
	
	WXPay wxPay = null;
	@PostConstruct
	public void init() {
		
		WXPayConfig config = new WXPayConfig() {
			
			public IWXPayDomain getWXPayDomain() {
				
				IWXPayDomain wxPayDomain = new IWXPayDomain() {
					
					@Override
					public void report(String domain, long elapsedTimeMillis, Exception ex) {
						
					}
					
					@Override
					public DomainInfo getDomain(WXPayConfig config) {
						
						DomainInfo info = new DomainInfo(WXPayConstants.DOMAIN_API, true);
						return info;
					}
					
				};
				return wxPayDomain;
			}
			
			public String getMchID() {
				
				return mahId;
			}
			
			@Override
			public String getKey() {
				
				return apiSecret;
			}
			
			public InputStream getCertStream() {
				
				try {
					return new FileInputStream(new File(certStreamPath));
				} catch (FileNotFoundException e) {
					logger.error("获取数据证书路径错误{}", certStreamPath);
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			public String getAppID() {
				
				return appId;
			}
		};
		try {
			wxPay = new WXPay(config, notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 支付授权
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo payAuth(@RequestBody RequestParame requestParame) throws BusinessRuntimeException {
		try {
			logger.info("收费开始,请求授权IP地址:{}",request.getRemoteAddr());
			String payPlatformType = requestParame.getData().get("payType");
			String deviceId = this.request.getParameter("deviceId");
			String priceId = this.request.getParameter("priceId");
			String ipAddress = request.getRemoteAddr();
			if (StringUtils.equals("WX", payPlatformType)){
				Map<String,String> signMap = new TreeMap<String,String>();
				signMap.put("deviceId", deviceId);
				signMap.put("priceId", priceId);
				signMap.put("ipAddress", ipAddress);
				String sign = WXPayUtil.generateSignature(signMap, apiSecret);
				System.out.print("签名：{}" + sign);
				String backUri = systeDomain + "/device/billing?deviceId=" + deviceId + "&priceId=" + priceId
				        + "&ipAddress=" + ipAddress + "&sign=" + sign;
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appId + "&redirect_uri="
				        + backUri + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
				logger.debug("授权地址：" + url);
				response.sendRedirect(url);
			} else {
				logger.warn("非微信支付");
				return new ResultInfo(ErrorCodeEnum.ES_1019.getErrorCode(),ErrorCodeEnum.ES_1019.getMessage(),null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("excute prePayment() system error:{}",e.getMessage()); 
			return new ResultInfo(ErrorCodeEnum.ES_1020.getErrorCode(),ErrorCodeEnum.ES_1020.getMessage(),null);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",null);
	}
	
	/**
	 * 创建预支付订单
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo createBilling(RequestParame requestParame) throws BusinessRuntimeException {
		logger.info("创建预支付订单");
		String deviceId = request.getParameter("deviceId");
		String priceId = request.getParameter("priceId");
		String code = request.getParameter("code");
		String sign = request.getParameter("sign");
		String ipAddress = request.getParameter("ipAddress");
		logger.info("微信中转请求:" + code);
		logger.info("微信中转请求IP地址：" + ipAddress);
		String exist = stringRedisTemplate.opsForValue().get(WEIXIN_CACHE_NAME);
		if (StringUtils.isNotBlank(exist)) {
			String backUri = systeDomain + "/device/complete/billing?deviceid=" + deviceId + "&priceid=" + priceId
			        + "&ipaddress=" + ipAddress + "&code=" + code;
			try {
				Map<String,String> signMap = new TreeMap<String,String>();
				signMap.put("deviceId", deviceId);
				signMap.put("priceId", priceId);
				signMap.put("ipAddress", ipAddress);
				signMap.put("sign", sign);
				if (WXPayUtil.isSignatureValid(signMap, apiSecret)) {
					logger.debug("中间签名通过！");
					signMap.put("code", code);
					backUri +="&sign=" + WXPayUtil.generateSignature(signMap, apiSecret);
					response.sendRedirect(backUri);
				} else {
					logger.debug("中间签名不通过！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("excute createBilling() system error:{}",e.getMessage()); 
				return new ResultInfo(ErrorCodeEnum.ES_1021.getErrorCode(),ErrorCodeEnum.ES_1021.getMessage(),null);
			}
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",null);
	}
	
	/**
	 * 支付请求
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo completeBilling(RequestParame requestParame) throws BusinessRuntimeException {
		logger.info("发起支付请求");
		String deviceId = request.getParameter("deviceid");
		String priceId = request.getParameter("priceid");
		String code = request.getParameter("code");
		String ipAddress = request.getParameter("ipaddress");
		String sign = request.getParameter("sign");
		String msg =  String.format(ErrorCodeEnum.ES_1023.getMessage(), "支付失败");
		String exist = stringRedisTemplate.opsForValue().get(WEIXIN_CACHE_NAME);
		if (StringUtils.isNotBlank(exist)) {
			logger.warn("授权码不可用,请求参数:{}",new Gson().toJson(requestParame));
			return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(),msg,null);
		}  else {
			// 向缓存中设置CODE
			stringRedisTemplate.opsForValue().set(WEIXIN_CACHE_NAME, code,7000L);
		}
		Map<String,String> signMap = new TreeMap<String,String>();
		signMap.put("deviceId", deviceId);
		signMap.put("priceId", priceId);
		signMap.put("ipAddress", ipAddress);
		signMap.put("code", code);
		signMap.put("sign", sign);
		try {
			if (!WXPayUtil.isSignatureValid(signMap, apiSecret)) {
				logger.debug("支付签名不通过！");
				return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(),msg,null);
			} else {
				logger.debug("支付签名不通过！");
			}
			logger.debug("设备订单请求代码:" + code);
			logger.debug("响应统一下单IP地址：" + ipAddress);
		} catch (Exception e) {
			logger.error("excute completeBilling() system error:{}",e.getMessage()); 
			return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(),msg,null);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",deviceId);
	}

	/**
	 * 生成或获取JSAPI凭证
	 * 
	 * @param code
	 * @param priceId
	 * @param ipAddress
	 * @param device
	 * @param priceValue
	 * @return
	 */
	@Override
	public ResultInfo createJsapi(String code,String priceId,String ipAddress,@RequestBody Device device,@RequestBody PriceValue priceValue) {
		logger.debug("获取JSAPI凭证,请求参数：{}",code);
		WeixinOauth2Token oauth2Token = WeiXinCommonUtil.getOauth2AccessToken(appId, appSecret, code);
		String msg =  String.format(ErrorCodeEnum.ES_1023.getMessage(), "支付失败");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if (oauth2Token == null) {
				logger.error("授权码已经被使用!");
				return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(), msg, null);
			}
			Map<String, String> resData = this.generateWeiXinBilling(device, priceValue,
					oauth2Token.getOpenId(), ipAddress);
			if (resData != null && resData.containsKey("return_code") && resData.containsKey("result_code")
			        && StringUtils.equals(resData.get("return_code"), WXPayConstants.SUCCESS)
			        && StringUtils.equals(resData.get("result_code"), WXPayConstants.SUCCESS)) {
				String nonceStr = WXPayUtil.generateNonceStr();
				String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
				Token token =  new Gson().fromJson(stringRedisTemplate.opsForValue().get("appToken"),Token.class);
				if (token != null) {
					logger.debug("获取Token缓存：" + token.getAccessToken());
				} else {
					token = WeiXinCommonUtil.getToken(appId, appSecret);
					stringRedisTemplate.opsForValue().set("appToken", new Gson().toJson(token),7000L);
					logger.debug("生成Token，并缓存：" + token.getAccessToken());
				}
				
				String appTicket = stringRedisTemplate.opsForValue().get("appTicket");
				String jsapiTicket = null;
				if (appTicket != null) {
					jsapiTicket = appTicket;
					logger.debug("获取JSAPI凭证：" + jsapiTicket);
				} else {
					jsapiTicket = WeiXinCommonUtil.getJsApiTicket(token.getAccessToken());
					stringRedisTemplate.opsForValue().set("appTicket", jsapiTicket,7000L);
					logger.debug("生成JSAPI凭证，并缓存：" + jsapiTicket);
				}
				Map<String, String> signature = new HashMap<String, String>();
				signature.put("jsapi_ticket", jsapiTicket);
				signature.put("noncestr", nonceStr);
				signature.put("timestamp", timestamp);
				String url = systeDomain + "/device/complete/billing?deviceid=" + device.getMachNo() + "&priceid=" + priceId
				        + "&ipaddress=" + ipAddress + "&code=" + code;
				signature.put("url", url);
				
				String sign = WXPayUtil.generateSignatureBySHA1(signature);
				logger.debug("页面配置签名：" + sign);
				resultMap.put("signature", sign);
				resultMap.put("appId", appId);
				resultMap.put("nonceStr", nonceStr);
				
				String packages = "prepay_id=" + resData.get("prepay_id");
				resultMap.put("packages", packages);
				resultMap.put("timeStamp", timestamp);
				resultMap.put("signType", WXPayConstants.MD5);
				
				Map<String, String> finalpackage = new TreeMap<String, String>();
				finalpackage.put("appId", appId);
				finalpackage.put("nonceStr", nonceStr);
				finalpackage.put("package", packages);
				finalpackage.put("signType", WXPayConstants.MD5);
				finalpackage.put("timeStamp", timestamp);
				String paySign = WXPayUtil.generateSignature(finalpackage, apiSecret, SignType.MD5);
				logger.debug("页面支付签名：" + paySign);
				resultMap.put("paySign", paySign);
				resultMap.put("billingNumber", resData.get("billingNumber"));
			} else if (resData.containsKey("return_code")
			        && StringUtils.equals(resData.get("return_code"), WXPayConstants.SUCCESS)) {
				logger.error("统一下单未成功，错误代码{}，错误信息{}", resData.get("err_code"), resData.get("err_code_des"));
				return new ResultInfo(ErrorCodeEnum.ES_1024.getErrorCode(),ErrorCodeEnum.ES_1024.getMessage(),null);
			} else if (resData.containsKey("return_code")
			        && StringUtils.equals(resData.get("return_code"), WXPayConstants.FAIL)) {
				logger.error("统一下单未成功，错误代码{}，错误信息{}", resData.get("return_code"), resData.get("return_msg"));
				return new ResultInfo(ErrorCodeEnum.ES_1024.getErrorCode(),ErrorCodeEnum.ES_1024.getMessage(),null);
			} else {
				logger.error("统一下单未成功，错误代码{}，错误信息{}", resData.get("return_code"), resData.get("return_msg"));
				return new ResultInfo(ErrorCodeEnum.ES_1024.getErrorCode(),ErrorCodeEnum.ES_1024.getMessage(),null);
			}
		} catch (Exception e) {
			logger.error("统一下单错误", e);
			return new ResultInfo(ErrorCodeEnum.ES_1025.getErrorCode(), ErrorCodeEnum.ES_1025.getMessage(), null);
		}
		//跳转jspai
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",resultMap);
	}
	
	private Map<String,String> generateWeiXinBilling(Device device,PriceValue priceValue, String openId, String ip){
		
		try {
			if (wxPay != null) {
				Map<String, String> reqData = new HashMap<String, String>();
				String billingNumber = this.createBilling(device, priceValue);
				reqData.put("device_info", device.getMachNo());
				reqData.put("body", body);
				reqData.put("spbill_create_ip", ip);
				reqData.put("out_trade_no", billingNumber);
				reqData.put("trade_type", "JSAPI");
				if (StringUtils.isNotBlank(openId)) {
					reqData.put("openid", openId);
				}
				reqData.put("total_fee", String.valueOf(Math.round(priceValue.getRealValue() * 100)));
				logger.debug("微信响应参数：" + reqData.toString());
				Map<String, String> resData = wxPay.unifiedOrder(reqData);
				logger.debug("微信响应结果：" + resData.toString());
				if (StringUtils.equals(resData.get("result_code"), WXPayConstants.SUCCESS)) {
					if (StringUtils.equals(resData.get("result_code"), WXPayConstants.SUCCESS)) {
						String weixin = resData.get("prepay_id");
						logger.debug("微信预支付ID:" + weixin);
						this.updateWeiXin(billingNumber, weixin);
					} else {
						String errorCode = resData.get("err_code");
						String errorMsg = resData.get("err_code_des");
						WeiXinNotity weiXinNotity = weiXinNotityRepository.findByOutTradeNo(billingNumber);
						if (weiXinNotity == null
								|| (!StringUtils.equals(weiXinNotity.getResultCode(), WXPayConstants.SUCCESS))) {
							this.updateBilling(billingNumber, null, Billing.BILLING_STATE_EXCEPTION, errorCode, errorMsg);
						} else {
							String weixin = resData.get("prepay_id");
							this.updateWeiXin(billingNumber, weixin);
						}
					}
				} else {
					String errorCode = resData.get("result_code");
					String errorMsg = resData.get("return_msg");
					WeiXinNotity weiXinNotity = weiXinNotityRepository.findByOutTradeNo(billingNumber);
					if (weiXinNotity == null
					        || (!StringUtils.equals(weiXinNotity.getResultCode(), WXPayConstants.SUCCESS))) {
						this.updateBilling(billingNumber, null, Billing.BILLING_STATE_EXCEPTION, errorCode,
						        errorMsg);
					} else {
						String weixin = resData.get("prepay_id");
						this.updateWeiXin(billingNumber, weixin);
					}
				}
				resData.put("billingNumber", billingNumber);
				return resData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建微信订单失败：{}",e.getMessage());
			return null;
		}
		return null;
	}
	
	private String createBilling(Device device, PriceValue priceValue) {
		Billing billing = new Billing();
		billing.setDeviceId(device.getId());
		billing.setBillingId(generateBillingNumber(device));
		billing.setMachNo(device.getMachNo());
		billing.setCostTime(priceValue.getCostTime());
		billing.setPriceId(priceValue.getId());
		billing.setAmount(priceValue.getRealValue());
		billing.setUnitPrice(priceValue.getValue());
		billing.setDiscount(priceValue.getDiscount());
		billing.setCreater("default");
		billingRepository.save(billing);
		return billing.getBillingId();
	}
	
	private String generateBillingNumber(Device device) {
		String billingNumber = device.getMachNo();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		return billingNumber + format.format(Calendar.getInstance().getTime());
	}
	
	private void updateWeiXin(String billingNumber, String weixin){
		Billing billing = billingRepository.findByBillingId(billingNumber);
		billing.setWeixin(weixin);
		billing.setState(Billing.BILLING_STATE_PENDING_PAYMENT);
		billingRepository.saveAndFlush(billing);
	}
	
	private void updateBilling(String billingNumber, String transactionId, Integer state, String errorCode,
	        String errorMsg){
		Billing billing = billingRepository.findByBillingId(billingNumber);
		billing.setState(state);
		if (StringUtils.isNotBlank(transactionId)) {
			billing.setTransactionId(transactionId);
		}
		if (StringUtils.isNotBlank(errorCode)) {
			billing.setErrorCode(errorCode);
		}
		if (StringUtils.isNotBlank(errorMsg)) {
			billing.setErrorMsg(errorMsg);
		}
		billingRepository.saveAndFlush(billing);
	}
	
	
	
	

}
