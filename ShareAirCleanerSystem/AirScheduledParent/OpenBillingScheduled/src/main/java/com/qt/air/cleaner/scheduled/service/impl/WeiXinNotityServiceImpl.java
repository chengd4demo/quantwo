package com.qt.air.cleaner.scheduled.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qt.air.cleaner.base.utils.CalculateUtils;
import com.qt.air.cleaner.scheduled.domain.Agent;
import com.qt.air.cleaner.scheduled.domain.Billing;
import com.qt.air.cleaner.scheduled.domain.BillingSuccess;
import com.qt.air.cleaner.scheduled.domain.Company;
import com.qt.air.cleaner.scheduled.domain.Device;
import com.qt.air.cleaner.scheduled.domain.Investor;
import com.qt.air.cleaner.scheduled.domain.ShareProfit;
import com.qt.air.cleaner.scheduled.domain.Trader;
import com.qt.air.cleaner.scheduled.domain.WeiXinNotity;
import com.qt.air.cleaner.scheduled.repository.AgentRepository;
import com.qt.air.cleaner.scheduled.repository.BillingRepository;
import com.qt.air.cleaner.scheduled.repository.DeviceRepository;
import com.qt.air.cleaner.scheduled.repository.ShareProfitRepository;
import com.qt.air.cleaner.scheduled.repository.WeiXinNotityCurdRepository;
import com.qt.air.cleaner.scheduled.service.AccountService;
import com.qt.air.cleaner.scheduled.service.WeiXinDownloadService;
import com.qt.air.cleaner.scheduled.service.WeiXinNotityService;

@Service
@Transactional
public class WeiXinNotityServiceImpl implements WeiXinNotityService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	DeviceRepository deviceRepository;
	@Resource
	WeiXinNotityCurdRepository weiXinNotityCurdRepository;
	@Resource
	BillingRepository billingRepository;
	@Autowired
	WeiXinDownloadService weiXinDownloadService;
	@Autowired
	ShareProfitRepository shareProfitRepository;
	@Autowired
	AgentRepository agentRepository;
	@Autowired
	AccountService accountService;
	private Map<String, Integer> gainProportion = new HashMap<String, Integer>();
	private Float free = 0.09f;
	@Value("${o2.billing.investor.gain.proportion}")
	public Integer investorGainProportion;
	@Value("${o2.billing.trader.gain.proportion}")
	public Integer traderGainProportion;
	@Value("${o2.billing.compnay.gain.proportion}")
	public Integer companyGainProportion;
	
	@PostConstruct
	public void init(){
		gainProportion.put(Investor.class.getSimpleName(), investorGainProportion);
		gainProportion.put(Trader.class.getSimpleName(), traderGainProportion);
		gainProportion.put(Company.class.getSimpleName(), companyGainProportion);
	}
	
	@Override
	public void startDownloadForSuccess(Date currentTime) {
		weiXinDownloadService.startDownloadForSuccess(currentTime);
	}
	
	/**
	 * 微信开帐供功能
	 * 
	 * @param currentTime
	 */
	@SuppressWarnings("unused")
	@Override
	public void updateWeixinNotityForOpenAccount(Date currentTime) {
		logger.debug("开始分账。。。。当前时间：" + dateFormat.format(currentTime));
		
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(currentTime);
		startTime.add(Calendar.DATE, -1);
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		
		logger.debug("微信通知开始时间：" + dateFormat.format(startTime.getTime()));
		
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(currentTime);
		endTime.add(Calendar.DATE, -1);
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 999);
		logger.debug("微信通知结束时间：" + dateFormat.format(endTime.getTime()));
		
		List<Integer> states = new ArrayList<Integer>();
		states.add(WeiXinNotity.WEIXIN_NOTITY_CONFIRM);
		states.add(WeiXinNotity.WEIXIN_NOTITY_EXCEPTION);
		
		List<WeiXinNotity> notities = queryAvailableWeiXinNotityInTimes(states, startTime.getTime(), endTime.getTime());
		logger.debug("当次分账的记录数量：" + notities.size() + "条");
		Billing billing = null;
		Float totalFee = 0.00f;
		for (WeiXinNotity notity : notities) {
			billing = billingRepository.findByBillingId(notity.getOutTradeNo());
			logger.debug("获取订单信息[" + notity.getOutTradeNo() + "]：" + billing != null ? billing.getDeviceId() : "不存在");
			if (billing != null) {
				totalFee = notity.getTotalFee();
				logger.debug("金额：" + totalFee);
				if (billing.getAmount().floatValue() == totalFee.floatValue()) {
					if (billing.getState() == Billing.BILLING_STATE_ACCOUNT_OPEN) {
						this.openBillingAccount(billing);
						notity.setState(WeiXinNotity.WEIXIN_NOTITY_OPEN);
						notity.setErrCodeDes("");
						billing.setState(Billing.BILLING_STATE_ACCOUNT_OPENED);
						billingRepository.saveAndFlush(billing);
						weiXinNotityCurdRepository.saveAndFlush(notity);
					} else {
						notity.setErrCodeDes("当前微信通知与对应账单未处于待开帐状态。");
						notity.setState(WeiXinNotity.WEIXIN_NOTITY_EXCEPTION);
						weiXinNotityCurdRepository.saveAndFlush(notity);
					}
				}
			} else {
				notity.setErrCodeDes("当前微信通知记录缺少对应账单记录。");
				notity.setState(WeiXinNotity.WEIXIN_NOTITY_EXCEPTION);
				weiXinNotityCurdRepository.saveAndFlush(notity);
			}
		}
	}
	
	@Override
	public void updateWeiXinStatusByDownload(Date currentTime) {
		logger.debug("开始分账。。。。当前时间：" + dateFormat.format(currentTime));
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(currentTime);
		startTime.add(Calendar.DATE, -1);
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		logger.debug("微信通知开始时间：" + dateFormat.format(startTime.getTime()));
		
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(currentTime);
		endTime.add(Calendar.DATE, -1);
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 999);
		logger.debug("微信通知结束时间：" + dateFormat.format(endTime.getTime()));
		
		List<Integer> states = new ArrayList<Integer>();
		states.add(WeiXinNotity.WEIXIN_NOTITY_NEW);
		
		List<WeiXinNotity> notities = queryAvailableWeiXinNotityInTimes(states, startTime.getTime(), endTime.getTime());
		logger.debug("当次对账的记录数量：" + notities.size() + "条");
		
		for (WeiXinNotity weiXinNotity : notities) {
			BillingSuccess billingSuccess = weiXinDownloadService.queryBillingSuccess(weiXinNotity.getTransactionId(),weiXinNotity.getOutTradeNo()
						,weiXinNotity.getDeviceInfo(),weiXinNotity.getTotalFee());
			if (billingSuccess == null) {
				continue;
			} else {
				logger.debug("对账成功：" + weiXinNotity.getOutTradeNo());
				weiXinNotity.setState(WeiXinNotity.WEIXIN_NOTITY_CONFIRM);
				weiXinNotityCurdRepository.saveAndFlush(weiXinNotity);
				this.updateBilling(weiXinNotity.getOutTradeNo(), weiXinNotity.getTransactionId(), Billing.BILLING_STATE_ACCOUNT_OPEN, null, null);
			}
			
		}
	}
	
	
	private void updateBilling(String billingNumber, String transactionId, Integer state, String errorCode,
	        String errorMsg) {
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

	private void openBillingAccount(Billing billing) {
		/**投资商开账逻辑*/
		Float totalAmount = billing.getAmount();
		Device device = deviceRepository.findById(billing.getDeviceId());
		//获取设定的分润比例
		Map<String,Integer> shareProfit = getDistributionRatio(device.getDistributionRatio());
		Investor investor = device.getInvestor();
		Integer proportion = shareProfit.get(Investor.class.getSimpleName());
		Integer costTimeInteger = billing.getCostTime() / 60;
		Float realyFree = this.free == 0.00f ? 0.09f : this.free;
		// 每小时扣除0.09耗材费用
		Float investorAmount = CalculateUtils.getInvestorAmount(totalAmount,proportion,costTimeInteger,realyFree);
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(investorAmount));
		investorAmount = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		if (investorAmount > 0.00f) {
			accountService.udpateInvestorAccount(billing, investor, investorAmount);
		}
		
		/**商家开账逻辑*/
		Trader trader = device.getTrader();
		proportion = shareProfit.get(Trader.class.getSimpleName());
		Float traderAmount = CalculateUtils.getTraderAmount(totalAmount, proportion);
		if (traderAmount > 0.00f) {
			accountService.updateTraderAccount(billing, trader, traderAmount);
		}
		
		/**代理开账逻辑*/
		//获取代理商id
		List<String> agentIds = getAgentIds(device.getDistributionRatio());
		Float agentAmount = 0.00f;
		//执行代理商开账业务
		if (agentIds.size() > 0 ) {
			String agentId = null;
			for(int i=0;i<agentIds.size();i++) {
				agentId = agentIds.get(i);
				agentAmount = openAgent(agentId,shareProfit,totalAmount,billing);
			}
		}
		/**公司开账逻辑*/
		Company company = device.getCompany();
		proportion = shareProfit.get(Company.class.getSimpleName());
		Float companyAmount = CalculateUtils.getCompanyAmount(totalAmount, proportion);
		Float unitComapnyAmount = new BigDecimal(Float.toString(CalculateUtils.sub(totalAmount, investorAmount)))
			.subtract(new BigDecimal(Float.toString(traderAmount))).subtract(new BigDecimal(Float.toString(agentAmount)))
			.setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		if (companyAmount < unitComapnyAmount) {
			companyAmount = unitComapnyAmount;
		}
		if (companyAmount > 0.00f)
		accountService.updateCompanyAccount(billing, company, companyAmount);
	}
	
	/**
	 * 代理商开张逻辑处理
	 * 
	 * @param agentId
	 * @param shareProfit
	 * @param totalAmount
	 * @param billingId
	 */
	private Float openAgent(String agentId,Map<String,Integer> shareProfit,Float totalAmount,Billing billing) {
		Agent agent = agentRepository.findByIdAndRemoved(agentId, Boolean.FALSE);
		String type = agent.getType();
		Integer proportion  = StringUtils.equals(ShareProfit.ACCOUNT_TYPE_AGENT_DL, type) ? shareProfit.get(Agent.class.getSimpleName()) 
				: shareProfit.get(Agent.class.getSimpleName() + ShareProfit.ACCOUNT_TYPE_AGENT_ZD);
		Float agentAmount = CalculateUtils.getAgentAmount(totalAmount, proportion);
		if (agentAmount > 0.00f) {
			accountService.updateAgentAccount(billing,agent, agentAmount);
		}
		return agentAmount;
	}

	/**
	 * 后去分润设置的代理商ID
	 * 
	 * @param distributionRatio
	 * @return
	 */
	private List<String> getAgentIds(String distributionRatio) {
		List<ShareProfit> shareProfits = shareProfitRepository.findByPlatformSet(distributionRatio);
		if (CollectionUtils.isEmpty(shareProfits)) {
			return new ArrayList<>();
		}
		List<String> result = new ArrayList<>();
		shareProfits.stream().forEach(p -> {
			if (StringUtils.isNotBlank(p.getAgentId()) && (StringUtils.equals(ShareProfit.ACCOUNT_TYPE_AGENT_ZD, p.getType()) || StringUtils.equals(ShareProfit.ACCOUNT_TYPE_AGENT_DL, p.getType()))) {
				result.add(p.getAgentId());
			} 
		});
		return result;
	}

	/**
	 * 获取设定的分润比例
	 * 
	 * @param distributionRatio
	 * @return
	 */
	private Map<String, Integer> getDistributionRatio(String distributionRatio) {
		List<ShareProfit> shareProfits = shareProfitRepository.findByPlatformSet(distributionRatio);
		if (CollectionUtils.isEmpty(shareProfits)) {
			return gainProportion;
		} 
		//分润比例数据小于最低配置条数使用默认配置比例 352（商户|投资方|公司）
		if(shareProfits.size()<3) {
			return gainProportion;
		}
		Map<String,Integer>  result = new HashMap<>();
		shareProfits.stream().forEach(s -> {
			String type = s.getType();
			if (StringUtils.equals(ShareProfit.ACCOUNT_TYPE_COMPANY, type)) {
				result.put(Company.class.getSimpleName(), s.getScale().intValue());
			}  else if (StringUtils.equals(ShareProfit.ACCOUNT_TYPE_AGENT_ZD, type)) {
				result.put(Agent.class.getSimpleName() + ShareProfit.ACCOUNT_TYPE_AGENT_ZD, s.getScale().intValue());
			} else if(StringUtils.equals(ShareProfit.ACCOUNT_TYPE_AGENT_DL, type)){
				result.put(Agent.class.getSimpleName(),s.getScale().intValue());
			} else if(StringUtils.equals(ShareProfit.ACCOUNT_TYPE_TRADER, type)) {
				result.put(Trader.class.getSimpleName(), s.getScale().intValue());
			}  else if(StringUtils.equals(ShareProfit.ACCOUNT_TYPE_INVESTOR, type)) {
				result.put(Investor.class.getSimpleName(), s.getScale().intValue());
				this.free = s.getFree();
			}
		});
		if(result.isEmpty()) {
			return gainProportion;
		}
		return result;
	}

	private List<WeiXinNotity> queryAvailableWeiXinNotityInTimes(List<Integer> states, Date startTime, Date endTime) {
		Specification<WeiXinNotity> querySpecifi = new Specification<WeiXinNotity>(){
			@Override
			public Predicate toPredicate(Root<WeiXinNotity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), startTime));
				predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
				predicates.add(cb.equal(root.get("removed"), false));
				In<Integer> in = cb.in(root.get("state"));
				for (Integer state : states) {
					 in.value(state);
				}
				predicates.add(in);
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		return weiXinNotityCurdRepository.findAll(querySpecifi);
	}

}
