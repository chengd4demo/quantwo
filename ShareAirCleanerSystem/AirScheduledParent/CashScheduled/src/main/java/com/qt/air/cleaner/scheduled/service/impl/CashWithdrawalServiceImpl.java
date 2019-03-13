package com.qt.air.cleaner.scheduled.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.qt.air.cleaner.base.utils.CalculateUtils;
import com.qt.air.cleaner.scheduled.domain.Account;
import com.qt.air.cleaner.scheduled.domain.AccountOutBound;
import com.qt.air.cleaner.scheduled.repository.AccountOutboundRepository;
import com.qt.air.cleaner.scheduled.service.CashWithdrawalService;

@Service
@Transactional
public class CashWithdrawalServiceImpl implements CashWithdrawalService {
	Logger logger = LoggerFactory.getLogger(CashWithdrawalServiceImpl.class);
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Value("${o2.wechat.subscription.name.description}")
	public String name;
	@Value("${o2.wechat.subscription.mah.id}")
	public String mahId;
	@Value("${o2.wechat.subscription.app.id}")
	public String appId;
	@Value("${o2.wechat.subscription.api.secret}")
	public String apiSecret;
	@Value("${o2.wechat.cert.stream.path}")
	public String certStreamPath;
	@Value("${o2.wechat.subscription.notify.url}")
	public String notifyUrl;
	@Value("${o2.wechat.red.pack.wishing}")
	public String redPackWishing;
	@Value("${o2.wechat.red.pack.act.name}")
	public String redPackActName;
	@Value("${o2.wechat.red.pack.remark}")
	public String redPackMark;
	@Value("${cash.withhold.taxes}")
	public Float withholdTaxes;
	@Resource
	AccountOutboundRepository accountOutboundRepository;
	protected  String ip;
	
	WXPay wxPay = null;
	@SuppressWarnings("rawtypes")
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
		
		Enumeration allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address && !ip.getHostAddress().equals("127.0.0.1")) {
					this.ip = ip.getHostAddress();
					break;
				}
			}
		}
	}
	
	@Override
	public void sendRedWithdrawal() {
		List<AccountOutBound> outBoundList = queryAccountOutbound();
		if(!CollectionUtils.isEmpty(outBoundList)) {
			Map<String, String> sendRedPakMap = null;
			try {
				Float amount = 0.00f;
				for(AccountOutBound outBound : outBoundList) {
					if(outBound.getAmount()>0.00f) {
						amount = (outBound.getAmount()) - (outBound.getAmount()*withholdTaxes);
						sendRedPakMap = sendRedPack(amount, outBound.getCreater(), ip, outBound.getBillingNumber());
						updateAccountOutBound(sendRedPakMap,outBound,true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("发送红包异常：" , e.getMessage());
			}
			
		}
		
	}

	/**
	 * 更新出账记录信息与总账
	 * 
	 * @param responseResult
	 * @param outBound
	 */
	private void updateAccountOutBound(Map<String, String> responseResult,AccountOutBound outBound,boolean isSend) {
		Account account = null;
		if(isSend) {
			//　开始解析微信结果，根据结果处理后续业务
			// return_code是通信标识，非交易标识，交易是否成功需要查看result_code来判断
			if (StringUtils.equals(responseResult.get("return_code"), WXPayConstants.SUCCESS)) {
				// result_code是交易标识，返回SUCCESS时为交易成功
				if (StringUtils.equals(responseResult.get("result_code"), WXPayConstants.SUCCESS)) {
					// 获取微信交易订单号
					String transactionId = responseResult.get("send_listid");
					// 关系出账记录为完成状态
					outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_COMPLETE);
					// 设置微信交易订单号
					outBound.setTransactionId(transactionId);
					// 更新账户出账记录和账务信息
					account = outBound.getAccount();
					account.setFreezingAmount(CalculateUtils.add(account.getFreezingAmount(), outBound.getAmount()));
				} else {
					String errorCode = responseResult.get("err_code");
					String errorMsg = responseResult.get("err_code_des");
					//异常账号
					if (StringUtils.equals("NO_AUTH", errorCode)) {
						logger.info("申请提现订单号：{}",outBound.getBillingNumber());
						logger.warn("该用户{}申请提现发放失败，此请求可能存在风险，已被微信拦截",outBound.getName());
						account = outBound.getAccount();
						account.setFreezingAmount(CalculateUtils.sub(account.getFreezingAmount(), outBound.getAmount()));
						account.setAvailableAmount(CalculateUtils.add(account.getAvailableAmount(), outBound.getAmount()));
						account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(), outBound.getAmount()));
						outBound.setAccount(account);
					}
					outBound.setErrorCode(errorCode);
					outBound.setErrorMsg(errorMsg);
					// 更新出账记录为错误状态，并设置此记录为无效
					outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_ERROR);
				}
			} else {
				String errorMsg = responseResult.get("return_msg");
				String errorCode = responseResult.get("result_code");
				logger.warn("提现发送现金红包失败：错误码:" + errorCode + "错误信息：" + errorMsg);
//				outBound.setErrorMsg(errorMsg);
				// 更新出账记录为错误状态，并设置此记录为无效
//				outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_ERROR);
				
			}
		} else {
			if (StringUtils.equals(responseResult.get("return_code"), WXPayConstants.SUCCESS)) {
				// result_code是交易标识，返回SUCCESS时为交易成功
				if (StringUtils.equals(responseResult.get("result_code"), WXPayConstants.SUCCESS)) {
					int status = getStatus(responseResult.get("status"));
					account = outBound.getAccount();
					if (status == AccountOutBound.ACCOUNT_OUT_BOUND_STATE_UNCLAIMED && outBound.getState() != AccountOutBound.ACCOUNT_OUT_BOUND_STATE_UNCLAIMED) {
						//未领取
						outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_UNCLAIMED);
						//红包回退商户账号需要把对应发送红包的商户金额重新计算(a.冻结金额减去 b.可用余额和总账加上)
						account.setFreezingAmount(CalculateUtils.sub(account.getFreezingAmount(), outBound.getAmount()));
						account.setAvailableAmount(CalculateUtils.add(account.getAvailableAmount(), outBound.getAmount()));
						account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(), outBound.getAmount()));
					} else if(status == AccountOutBound.ACCOUNT_OUT_BOUND_STATE_RECEIVE){
						//已领取
						outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_RECEIVE);
						account.setFreezingAmount(CalculateUtils.sub(account.getFreezingAmount(), outBound.getAmount()));
					} else {
						//其它
						outBound.setState(status);
					}
				} 
			}
		}
		accountOutboundRepository.saveAndFlush(outBound);
	}

	/**
	 * 获取发送状态
	 * 
	 * @param status
	 * @return
	 */
	private int getStatus(String status) {
		int result = 0;
		switch (status) {
		case WXPayConstants.SENDING:
			result = AccountOutBound.ACCOUNT_OUT_BOUND_STATE_SEND_ING;
			break;
		case WXPayConstants.SENT:
			result = AccountOutBound.ACCOUNT_OUT_BOUND_STATE_WATINT_RECEVIE;
			break;
		case WXPayConstants.FAILED:
			result = AccountOutBound.ACCOUNT_OUT_BOUND_STATE_FAIL;
			break;

		case WXPayConstants.RECEIVED:
			result = AccountOutBound.ACCOUNT_OUT_BOUND_STATE_RECEIVE;
			break;
		case WXPayConstants.RFUND_ING:
			result = AccountOutBound.ACCOUNT_OUT_BOUND_STATE_UNCLAIMED;
			break;
		case WXPayConstants.REFUND:
			result = AccountOutBound.ACCOUNT_OUT_BOUND_STATE_UNCLAIMED;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 发送微信红包接口
	 * 
	 * @param cashAmount
	 * @param weiXin
	 * @param ip
	 * @param billingNumber
	 * @return
	 * @throws Exception 
	 */
	private Map<String, String> sendRedPack(Float cashAmount, String weiXin, String ip, String billingNumber) throws Exception {
		// 参考API文档：https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_4&index=3
		Map<String, String> reqData = new HashMap<String, String>();
		//随机字符串
		reqData.put("nonce_str", WXPayUtil.generateNonceStr());
		// 商户订单号
		reqData.put("mch_billno", billingNumber);
		//商户号
		reqData.put("mch_id", mahId);
		//公众号ID
		reqData.put("wxappid", appId);
		// 商户名称
		reqData.put("send_name", name);
		// 用户openid
		reqData.put("re_openid", weiXin);
		// 付款金额
		reqData.put("total_amount", String.valueOf(Math.round(cashAmount * 100)));
		// 红包发放总人数
		reqData.put("total_num", "1");
		// Ip地址
		reqData.put("client_ip", ip);
		// 红包祝福语
		reqData.put("wishing", redPackWishing);
		// 场景id
		reqData.put("scene_id", "PRODUCT_5");
		// 活动名称
		reqData.put("act_name", redPackActName);
		// 备注
		reqData.put("remark", redPackMark);
		logger.info("微信现金红包请求参数：" + reqData.toString());
		return wxPay.sendRedPack(reqData);
	}

	/**
	 * 获取出账信息审核通过的和发送失败的数据
	 * 
	 * @return
	 */
	private List<AccountOutBound> queryAccountOutbound() {
		List<AccountOutBound> result = new ArrayList<>();
		AccountOutBound outBound = new AccountOutBound();
		outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_AUDIT);
		Example<AccountOutBound> example = Example.of(outBound);
		result.addAll(accountOutboundRepository.findAll(example));
		outBound = new AccountOutBound();
		outBound.setState(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_FAIL);
		example = Example.of(outBound);
		result.addAll(accountOutboundRepository.findAll(example));
		return result;
	}
	
	@Override
	public void updateRedWithdrawalState() {
		Calendar startTime = Calendar.getInstance();
		logger.debug("开始查询红包记录。。。。当前时间：" + dateFormat.format(startTime.getTime()));
		startTime.add(Calendar.DATE, -1);
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		
		logger.debug("查询红包开始时间：" + dateFormat.format(startTime.getTime()));
		
		Calendar endTime = Calendar.getInstance();
		logger.debug("查询红包开始时间：" + dateFormat.format(endTime.getTime()));
		List<Integer> states = new ArrayList<Integer>();
		states.add(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_COMPLETE);
		states.add(AccountOutBound.ACCOUNT_OUT_BOUND_STATE_FAIL);
		List<AccountOutBound>  outBoundList = queryAccountOutBoundInTimes(states,startTime.getTime(),endTime.getTime());
		if(!CollectionUtils.isEmpty(outBoundList)) {
			Map<String,String> getbinfoMap = null;
			try {
				for(AccountOutBound outBound : outBoundList) {
					getbinfoMap = gethbInfo(ip,outBound.getBillingNumber());
					updateAccountOutBound(getbinfoMap,outBound,false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询红包记录异常：", e.getMessage());
			}
		}
		
		
	}
	
	private Map<String, String> gethbInfo(String ip,String billingNumber) throws Exception {
		Map<String, String> reqData = new HashMap<String, String>();
		//随机字符串
		reqData.put("nonce_str", WXPayUtil.generateNonceStr());
		// 商户订单号
		reqData.put("mch_billno", billingNumber);
		//商户号
		reqData.put("mch_id", mahId);
		//公众号ID
		reqData.put("appid", appId);
		// 订单类型
		reqData.put("bill_type", "MCHT");
		logger.info("微信现金红包查询请求参数：" + reqData.toString());
		return wxPay.gethbInfo(reqData);
	}
	
	private List<AccountOutBound> queryAccountOutBoundInTimes(List<Integer> states, Date startTime, Date endTime) {
		Specification<AccountOutBound> querySpecifi = new Specification<AccountOutBound>(){
			@Override
			public Predicate toPredicate(Root<AccountOutBound> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.greaterThanOrEqualTo(root.get("lastOperateTime"), startTime));
				predicates.add(cb.lessThanOrEqualTo(root.get("lastOperateTime"), endTime));
				predicates.add(cb.equal(root.get("removed"), false));
				In<Integer> in = cb.in(root.get("state"));
				for (Integer state : states) {
					 in.value(state);
				}
				predicates.add(in);
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		return accountOutboundRepository.findAll(querySpecifi);
	}
	
}
