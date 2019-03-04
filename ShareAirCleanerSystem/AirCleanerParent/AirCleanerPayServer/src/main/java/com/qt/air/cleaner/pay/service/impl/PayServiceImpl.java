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
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.qt.air.cleaner.pay.repository.DeviceRepository;
import com.qt.air.cleaner.pay.repository.PriceRepository;
import com.qt.air.cleaner.pay.repository.WeiXinNotityRepository;
import com.qt.air.cleaner.pay.service.PayService;
import com.qt.air.cleaner.pay.utils.BankUtil;
import com.qt.air.cleaner.pay.utils.DeviceUtil;
import com.qt.air.cleaner.pay.utils.WeiXinCommonUtil;
import com.qt.air.cleaner.pay.vo.CompleteBilling;


@RestController
@Transactional
public class PayServiceImpl implements PayService {
	private final static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	private HttpServletRequest request;
	@Resource
	private BillingRepository billingRepository;
	@Resource
	private WeiXinNotityRepository weiXinNotityRepository;
	@Resource
	private DeviceRepository deviceRepository;
	@Resource
	private PriceRepository priceRepository;
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
		String url = "";
		String payPlatformType = requestParame.getData().get("payType");
		String deviceId = requestParame.getData().get("deviceId");
		String priceId = requestParame.getData().get("priceId");
		String ipAddress = request.getRemoteAddr();
		try {
			if (StringUtils.equals("WX", payPlatformType)){
				Map<String,String> signMap = new TreeMap<String,String>();
				logger.info("收费开始,请求授权IP地址:{}",ipAddress);
				logger.info("收费开始,请求价格ID:{}",priceId);
				logger.info("收费开始,请求设备MachNO:{}",deviceId);
				signMap.put("deviceId", deviceId);
				signMap.put("priceId", priceId);
				signMap.put("ipAddress", ipAddress);
				String sign = WXPayUtil.generateSignature(signMap, apiSecret);
				System.out.print("签名：{}" + sign);
				String backUri = systeDomain + "?deviceId=" + deviceId + "&priceId=" + priceId
				        + "&ipAddress=" + ipAddress + "&sign=" + sign;
				backUri = WeiXinCommonUtil.urlEncodeUTF8(backUri);
				url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appId + "&redirect_uri="
				        + backUri + "&response_type=code&scope=snsapi_base&state=deviceBilling&connect_redirect=1#wechat_redirect";
				logger.info("授权地址：" + url);
			} else {
				logger.warn("非微信支付");
				return new ResultInfo(ErrorCodeEnum.ES_1019.getErrorCode(),ErrorCodeEnum.ES_1019.getMessage(),null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("excute prePayment() system error:{}",e.getMessage()); 
			return new ResultInfo(ErrorCodeEnum.ES_1020.getErrorCode(),ErrorCodeEnum.ES_1020.getMessage(),null);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",url);
	}
	
	/**
	 * 创建预支付订单前签名验证
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo createBilling(@RequestBody RequestParame requestParame) throws BusinessRuntimeException {
		logger.info("创建预支付订单,请求参数:{}",new Gson().toJson(requestParame));
		String deviceId = requestParame.getData().get("deviceId");
		String priceId = requestParame.getData().get("priceId");
		String code = requestParame.getData().get("code");
		String sign = requestParame.getData().get("sign");
		String ipAddress = requestParame.getData().get("ipAddress");
		logger.info("微信中转请求:" + code);
		logger.info("微信中转请求IP地址：" + ipAddress);
		String exist = stringRedisTemplate.opsForValue().get(WEIXIN_CACHE_NAME);
		String backUri = systeDomain + "?deviceid=" + deviceId + "&priceid=" + priceId
		        + "&ipaddress=" + ipAddress + "&code=" + code+"&state=completeBilling";
		if (exist == null) {
			try {
				Map<String,String> signMap = new TreeMap<String,String>();
				signMap.put("deviceId", deviceId);
				signMap.put("priceId", priceId);
				signMap.put("ipAddress", ipAddress);
				signMap.put("sign", sign);
				logger.info("验证签名成功后业务请求地址：" + backUri);
				if (WXPayUtil.isSignatureValid(signMap, apiSecret)) {
					logger.info("中间签名通过！");
					signMap.put("code", code);
					backUri +="&sign=" + WXPayUtil.generateSignature(signMap, apiSecret);
				} else {
					logger.info("中间签名不通过！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("excute createBilling() system error:{}",e.getMessage()); 
				return new ResultInfo(ErrorCodeEnum.ES_1021.getErrorCode(),ErrorCodeEnum.ES_1021.getMessage(),null);
			}
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",backUri);
	}
	
	/**
	 * 支付请求
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo completeBilling(@RequestBody RequestParame requestParame) throws BusinessRuntimeException {
		logger.info("发起支付请求,请求参数:{}",new Gson().toJson(requestParame));
		String deviceId = requestParame.getData().get("deviceId");
		String priceId = requestParame.getData().get("priceId");
		String code = requestParame.getData().get("code");
		String ipAddress = requestParame.getData().get("ipAddress");
		String sign = requestParame.getData().get("sign");
		String msg =  String.format(ErrorCodeEnum.ES_1023.getMessage(), "支付失败");
		logger.info("缓存key:{}",WEIXIN_CACHE_NAME);
		String exist = stringRedisTemplate.opsForValue().get(WEIXIN_CACHE_NAME);
		if (StringUtils.isNotBlank(exist)) {
			logger.warn("授权码不可用,请求参数:{}",new Gson().toJson(requestParame));
			return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(),msg,null);
		}  else {
			// 向缓存中设置CODE
			stringRedisTemplate.opsForValue().set(WEIXIN_CACHE_NAME, code,1000L,TimeUnit.MILLISECONDS);
		}
		Map<String,String> signMap = new TreeMap<String,String>();
		signMap.put("deviceId", deviceId);
		signMap.put("priceId", priceId);
		signMap.put("ipAddress", ipAddress);
		signMap.put("code", code);
		signMap.put("sign", sign);
		logger.info("签名验证参数:{}" , new Gson().toJson(signMap));
		logger.info("apiSecret签名Key:{}",apiSecret);
		try {
			if (!WXPayUtil.isSignatureValid(signMap, apiSecret)) {
				logger.info("支付签名不通过！");
				return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(),msg,null);
			} else {
				logger.info("支付签名通过！");
			}
			logger.info("设备订单请求代码:" + code);
			logger.info("响应统一下单IP地址：" + ipAddress);
			Device device = deviceRepository.findByDeviceSequence(deviceId);
			if (device == null) {
				logger.error("空气净化器【{}】未注册！",deviceId);
			} 
			PriceValue priceValue = priceRepository.findOne(priceId);
			if (priceValue == null) {
				logger.error("价格参数不可用！");
			}
			return this.createJsapi(new CompleteBilling(code, priceId, ipAddress, device, priceValue));
		} catch (Exception e) {
			logger.error("excute completeBilling() system error:{}",e.getMessage()); 
			e.printStackTrace();
			return new ResultInfo(ErrorCodeEnum.ES_1023.getErrorCode(),msg,null);
		}
	}
	/**
	 * 生成或获取JSAPI凭证
	 * 
	 * @param completeBilling
	 * @return
	 */
	public ResultInfo createJsapi(CompleteBilling completeBilling) {
		
		logger.info("获取JSAPI凭证,请求参数：{}",new Gson().toJson(completeBilling));
		String code = completeBilling.getCode();
		String priceId = completeBilling.getPriceId();
		String ipAddress = completeBilling.getIpAddress();
		Device device= completeBilling.getDevice();
		PriceValue priceValue = completeBilling.getPriceValue();
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
				Token token = stringRedisTemplate.opsForValue().get("appToken") == null ? null :  new Gson().fromJson(stringRedisTemplate.opsForValue().get("appToken").toString(), Token.class);
				if (token != null) {
					logger.debug("获取Token缓存：" + token.getAccessToken());
				} else {
					token = WeiXinCommonUtil.getToken(appId, appSecret);
					stringRedisTemplate.opsForValue().set("appToken", new Gson().toJson(token),7000L,TimeUnit.SECONDS);
					logger.debug("生成Token，并缓存：" + token.getAccessToken());
				}
				
				String appTicket = stringRedisTemplate.opsForValue().get("appTicket");
				String jsapiTicket = null;
				if (appTicket != null) {
					jsapiTicket = appTicket;
					logger.debug("获取JSAPI凭证：" + jsapiTicket);
				} else {
					jsapiTicket = WeiXinCommonUtil.getJsApiTicket(token.getAccessToken());
					stringRedisTemplate.opsForValue().set("appTicket", jsapiTicket,7000L,TimeUnit.SECONDS);
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
				reqData.put("device_info", device.getDeviceSequence());
				reqData.put("body",body);
				reqData.put("spbill_create_ip", ip);
				reqData.put("out_trade_no", billingNumber);
				reqData.put("trade_type", "JSAPI");
				if (StringUtils.isNotBlank(openId)) {
					reqData.put("openid", openId);
				}
				reqData.put("total_fee", String.valueOf(Math.round(priceValue.getRealValue() * 100)));
				logger.info("微信响应参数：" + reqData.toString());
				Map<String, String> resData = wxPay.unifiedOrder(reqData);
				logger.info("微信响应结果：" + resData.toString());
				if (StringUtils.equals(resData.get("result_code"), WXPayConstants.SUCCESS)) {
					if (StringUtils.equals(resData.get("result_code"), WXPayConstants.SUCCESS)) {
						String weixin = resData.get("prepay_id");
						logger.info("微信预支付ID:" + weixin);
						this.updateWeiXin(billingNumber, weixin, openId);
					} else {
						String errorCode = resData.get("err_code");
						String errorMsg = resData.get("err_code_des");
						WeiXinNotity weiXinNotity = weiXinNotityRepository.findByOutTradeNo(billingNumber);
						if (weiXinNotity == null
								|| (!StringUtils.equals(weiXinNotity.getResultCode(), WXPayConstants.SUCCESS))) {
							this.updateBilling(billingNumber, null, Billing.BILLING_STATE_EXCEPTION, errorCode, errorMsg);
						} else {
							String weixin = resData.get("prepay_id");
							this.updateWeiXin(billingNumber, weixin, openId);
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
						this.updateWeiXin(billingNumber, weixin, openId);
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
		billing.setWeixin("");
		logger.info("微信设备消费记录预支付订单插入：{}",billing.toString());
		billingRepository.saveAndFlush(billing);
		return billing.getBillingId();
	}
	
	private String generateBillingNumber(Device device) {
		String billingNumber = device.getMachNo();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		return billingNumber + format.format(Calendar.getInstance().getTime());
	}
	
	private void updateWeiXin(String billingNumber, String weixin,String openId){
		Billing billing = billingRepository.findByBillingId(billingNumber);
		billing.setWeixin(weixin);
		billing.setCreater(openId);
		billing.setState(Billing.BILLING_STATE_PENDING_PAYMENT);
		logger.info("设备消费记录：{}",billing.toString());
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
	
	/**
	 * 微信回调通知
	 * 
	 * @param parame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo billingNotify(@RequestParam("strXML") String strXML) throws BusinessRuntimeException {
		logger.info("微信回调通知,请求参数：{}",strXML);
		if(StringUtils.isNotBlank(strXML)) {
			try {
				logger.info("微信回调通知内容：{}",strXML);
				Map<String, String> xmlData = WXPayUtil.xmlToMap(strXML);
				this.updateWeiXinNotify(xmlData);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("微信回调通知处理异常：{}",e.getMessage());
			}
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",null);
	}
	
	private void updateWeiXinNotify(Map<String, String> xmlData) {
		try {
			WeiXinNotity notity = WeiXinNotity.toWeiXinNotity(xmlData);
			logger.info("微信通知内容：", notity.toString());
			if (StringUtils.equals(notity.getResultCode(), "SUCCESS")
			        && StringUtils.equals(notity.getReturnCode(), "SUCCESS")) {
				logger.info("支付功能！");
				if (WXPayUtil.isSignatureValid(xmlData, apiSecret)) {
					logger.info("签名验证通过！");
					notity.setState(WeiXinNotity.WEIXIN_NOTITY_NEW);
					weiXinNotityRepository.saveAndFlush(notity);
					Billing billing = billingRepository.findByBillingId(notity.getOutTradeNo());
					billing.setState(Billing.BILLING_STATE_ACCOUNT_OPEN);
					billing.setCreater(notity.getOpenId());
					String transactionId = notity.getTransactionId();
					if (StringUtils.isNotBlank(notity.getTransactionId())) {
						billing.setTransactionId(transactionId);
					}
					billingRepository.saveAndFlush(billing);
					int costTimeSecond = billing.getCostTime() * 60;
					logger.info("当前开启设备时长为：{}",costTimeSecond);
					int surplusConstTimeSecond = billingRepository.getSurplusConstTime(billing.getMachNo(),notity.getOutTradeNo()) * 60;
					logger.info("当前设备剩余时长为：{}",surplusConstTimeSecond);
					costTimeSecond += surplusConstTimeSecond; //追加时间
					logger.info("实际开启时长为：{}",costTimeSecond);
					
					logger.info("");
					DeviceUtil.turnOnDevice(billing.getMachNo(), costTimeSecond);
				}
			} else {
				logger.info("微信通知内容参数错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ResultInfo weiXinMsg(@RequestParam("type") String type,@RequestParam("billingNumber") String billingNumber) throws BusinessRuntimeException {
		logger.info("支付信息处理{}类型{}", billingNumber, type);
		Map<String, String> reqData = new HashMap<String, String>();
		if (StringUtils.equals("success", type)) {
			Billing billing = billingRepository.findByBillingId(billingNumber);
			if (billing != null) {
				try {
					reqData.put("transaction_id", billing.getTransactionId());
					wxPay.fillRequestData(reqData);
					Map<String, String> queryResult = wxPay.orderQuery(reqData);
					queryResult.put("body", body);
					queryResult.put("backType", BankUtil.getbankType(queryResult.get("bank_type")));
					reqData.putAll(queryResult);
					reqData.put("cashTime", billing.getCostTime().toString());
					reqData.put("wxMsgType", "success");
				} catch (Exception e) {
					e.printStackTrace();
					reqData.put("err_code", "BILLING_NO_EXIST");
					reqData.put("err_code_des", "获取支付结果中...");
					reqData.put("wxMsgType", "success");
					return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",reqData);
				}
			} else {
				reqData.put("err_code", "BILLING_NO_EXIST");
				reqData.put("err_code_des", "BILLING_NO_EXIST");
			}
			reqData.put("wxMsgType", "success");
		} else if (StringUtils.equals(type, "cancel")) {
			this.updateBilling(billingNumber, null, Billing.BILLING_STATE_CANCEL, null, null);
			reqData.put("wxMsgType", "cancel");
		} else if (StringUtils.equals(type, "fail")) {
			this.updateBilling(billingNumber, null, Billing.BILLING_STATE_EXCEPTION, null, null);
			reqData.put("wxMsgType", "fail");
		} else {
			reqData.put("wxMsgType", "other");
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",reqData);
	}

	@Override
	public int getSurplusConstTime(String machNo,String outTradeNo) {
		return billingRepository.getSurplusConstTime(machNo,outTradeNo);
	}

}
