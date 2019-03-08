package com.qt.air.cleaner.accounts.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.accounts.domain.Account;
import com.qt.air.cleaner.accounts.domain.AccountOutBound;
import com.qt.air.cleaner.accounts.domain.Company;
import com.qt.air.cleaner.accounts.domain.Investor;
import com.qt.air.cleaner.accounts.domain.OutBoundRejectReason;
import com.qt.air.cleaner.accounts.domain.Trader;
import com.qt.air.cleaner.accounts.repository.AccountOutboundRepository;
import com.qt.air.cleaner.accounts.repository.AccountRepository;
import com.qt.air.cleaner.accounts.repository.CompanyRepository;
import com.qt.air.cleaner.accounts.repository.InvestorRepository;
import com.qt.air.cleaner.accounts.repository.OutBoundRejectReasonRepository;
import com.qt.air.cleaner.accounts.repository.TraderRepository;
import com.qt.air.cleaner.accounts.service.AccountService;
import com.qt.air.cleaner.accounts.vo.AccountDto;
import com.qt.air.cleaner.accounts.vo.AccountOutboundDto;
import com.qt.air.cleaner.base.contant.CommonConst;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.enums.AccountOutBoundEnum;
import com.qt.air.cleaner.base.enums.ErrorCodeEnum;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.base.utils.CalculateUtils;



@RestController
@Transactional
public class AccountServiceImpl implements AccountService {
	private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Value("${o2.wechat.subscription.mah.id}")
	public String mahId;
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSSS"); 
	@Resource
	private TraderRepository traderRepository;
	@Resource
	private InvestorRepository investorRepository;
	@Resource
	private CompanyRepository companyRepository;
	@Resource
	private AccountOutboundRepository accountOutboundRepository;
	@Resource
	private AccountRepository accountRepository;
	@Resource
	private OutBoundRejectReasonRepository outBoundRejectReasonRepository;
	
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	@Override
	public ResultInfo queryAccountDetailByWeixin(@PathVariable String weixin) {
		logger.info("execute method queryAccountDetailByWeixin() param --> weixin:{}", weixin);
		Map<String,Float> result = new HashMap<String,Float>();
		List<AccountDto> dtoes = new ArrayList<>(3);
		Trader trader = traderRepository.findByWeixin(weixin);
		if(trader != null) {
			dtoes.add(new AccountDto(trader, trader.getAccount()));
		}
		Investor investor = investorRepository.findByWeixin(weixin);
		if(investor != null) {
			dtoes.add(new AccountDto(investor, investor.getAccount()));
		}
		Company company = companyRepository.findByWeixin(weixin);
		if(company != null) {
			dtoes.add(new AccountDto(company, company.getAccount()));
		}
		Float totalAcmount = 0.00f;
		Float availableAmount = 0.00f;
		if (!CollectionUtils.isEmpty(dtoes)) {
			for (AccountDto dtoe : dtoes) {
				totalAcmount += dtoe.getTotalAmount();
				availableAmount += dtoe.getAvailableAmount();
			}
		}
		result.put("totalAcmount",totalAcmount);
		result.put("availableAmount", availableAmount);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", result);
	}
	
	/**
	 * 提现记录
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResultInfo queryAccountOutboundPage(@RequestBody RequestParame requestParame)  throws BusinessRuntimeException {
		logger.info("execute method queryAccountOutbound() param --> weixin:{}", new Gson().toJson(requestParame));
		String weixin = requestParame.getData().get("weixin");
		int state = Integer.valueOf(requestParame.getData().get("state"));
		List<AccountOutboundDto>  result = new ArrayList<>(30);
		if(StringUtils.isNotBlank(weixin)) {
			StringBuffer sql = new StringBuffer();
			sql.append("select createdate,amount,state,id,showbutton");
			sql.append(" from (select row_.*, rownum rownum_");
			sql.append("	          from (select to_char(t.create_time,'yyyy-mm-dd hh24:mi:ss') createdate,");
			sql.append("                       t.amount,t.id, case when(t.state = 0) then 1 else 0 end showbutton,");
			sql.append("                       case");
			sql.append("                         when ((t.state = 0 or t.state = 1 or t.state = 2) and t.removed != 'Y') then '处理中'");
			sql.append("                         when (t.state = 6 and t.removed != 'Y') then  '未领取'");
			sql.append("                         when (t.state = 4 and t.removed != 'Y') then  '已完成'");
			sql.append("                         when t.removed = 'Y' then '已取消'");
			sql.append("                         else '其它' end state");
			sql.append("                    from ACT_ACCOUNT_OUTBOUND t");
			sql.append("                       where t.weixin = :weixin");
			if (AccountOutBoundEnum.CANCELLED.getStatus() == state){
				sql.append("                       and t.removed = :removed");
			} 
			if (AccountOutBoundEnum.REQUEST.getStatus() == state || AccountOutBoundEnum.UNCOLLECTED.getStatus() == state
					|| AccountOutBoundEnum.COMPLETED.getStatus() == state || AccountOutBoundEnum.NOTPASS.getStatus() == state) {
				sql.append("                       and t.state in (:state) and t.removed='N'");
			}
			sql.append("                	order by t.create_time desc) row_");
			sql.append("        where rownum <= :end )");
			sql.append("where rownum_ > :start");
			EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sql.toString())
					.addScalar("createdate",StandardBasicTypes.STRING)
					.addScalar("amount",StandardBasicTypes.FLOAT)
					.addScalar("state",StandardBasicTypes.STRING)
					.addScalar("id",StandardBasicTypes.STRING)
					.addScalar("showbutton",StandardBasicTypes.BOOLEAN);
			query.setParameter("weixin", weixin);
			
			if (AccountOutBoundEnum.UNCOLLECTED.getStatus() == state) {
				query.setParameterList("state", CommonConst.UNCOLLECTED_STATES);
			} else if (AccountOutBoundEnum.CANCELLED.getStatus() == state){
				query.setParameter("removed", "Y");
			} else if (AccountOutBoundEnum.COMPLETED.getStatus() == state){
				query.setParameterList("state", CommonConst.COMPLETED_STATES);
			} else if (AccountOutBoundEnum.NOTPASS.getStatus() == state){
				query.setParameterList("state", CommonConst.NOPASS_STATES);
			} else {
				query.setParameterList("state", CommonConst.REQUEST_STATES);
			}
			
			Integer start = requestParame.getPage().getStart();
			Integer end = requestParame.getPage().getEnd();
			query.setParameter("start", start);
			query.setParameter("end", end);
			query.setResultTransformer(Transformers.aliasToBean(AccountOutboundDto.class));
			result = query.list();
			em.close();
		} else {
			throw new BusinessRuntimeException(ResultCode.R2001.code,ResultCode.R2001.info);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",result);
	}
	
	/**
	 * 取消提现
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public ResultInfo cleanAccountOutbound(@PathVariable String id) {
		logger.info("execute method cleanAccountOutbound() param --> id:{}", id);
		AccountOutBound accountOutBound = null;
		accountOutBound = accountOutboundRepository.findByIdAndRemoved(id, false);
		Integer state = accountOutBound.getState();
		//用户取消,回滚冻结金额、可用余额、总额
		if(state == 0 && StringUtils.isNotBlank(id)) {
				Account account = accountOutBound.getAccount();
				account.setFreezingAmount(CalculateUtils.sub(account.getFreezingAmount(), accountOutBound.getAmount()));
				account.setAvailableAmount(CalculateUtils.add(account.getAvailableAmount(), accountOutBound.getAmount()));
				account.setTotalAmount(CalculateUtils.add(account.getTotalAmount(), accountOutBound.getAmount()));
				accountOutBound.setAccount(account);
				accountOutboundRepository.saveAndFlush(accountOutBound);
				accountOutboundRepository.cancellUpdate(id, new java.util.Date());
				return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", id);
		} else {
			return new ResultInfo(ErrorCodeEnum.ES_1029.getErrorCode(),ErrorCodeEnum.ES_1029.getMessage(),null);
		}
	}
	
	/**
	 * 申请提现 
	 * 
	 * @param parames
	 * @return
	 * @throws BusinessRuntimeException
	 */
	@Override
	public ResultInfo applyForAccountOutbound(@RequestBody Map<String, String> parames)throws BusinessRuntimeException {
		logger.info("execute method applyForAccountOutbound() param --> parames:{}", parames);
		String weixin = parames.get("weixin");
		Float amount = Float.valueOf(parames.get("amount"));
		String type = parames.get("userType");
		boolean  applayIsOk = checkApplayPassword(parames.get("password"),type,weixin);
		if(applayIsOk) {
			Account account = this.getUserInfo(type, weixin);
			 //判断提现金额是否大于可用余额
			if(amount<=account.getAvailableAmount() && amount<=account.getTotalAmount()) {
				 saveOutBound(parames);
			}else {
				return new ResultInfo(ErrorCodeEnum.ES_1028.getErrorCode(),ErrorCodeEnum.ES_1028.getMessage(),null);
			}
		} else {
			return new ResultInfo(ErrorCodeEnum.ES_1026.getErrorCode(),ErrorCodeEnum.ES_1026.getMessage(),null);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK),"success",parames);
	}

	private Account updateAmount(AccountOutBound outBound) {
		Account account = null;
		String weixin = outBound.getWeixin();
		String type = outBound.getType();
		if(StringUtils.equals(Account.ACCOUNT_TYPE_TRADER, type)) {
			Trader trader = traderRepository.findByWeixin(weixin);
			if (trader != null) {
				account = trader.getAccount();
			} 
		} else if(StringUtils.equals(Account.ACCOUNT_TYPE_COMPANY, type)) {
			Company company = companyRepository.findByWeixin(weixin);
			if (company != null) {
				account = company.getAccount();
			} 
		} else if(StringUtils.equals(Account.ACCOUNT_TYPE_INVESTOR, type)) {
			Investor investor = investorRepository.findByWeixin(weixin);
			if (investor != null) {
				account = investor.getAccount();
			} 
		}
		
		/**
		 * TODO 可能出现业务问题
		 */
		Float freeAmount = CalculateUtils.add(account.getFreezingAmount(), outBound.getAmount());
		Float availableAmount = CalculateUtils.sub(account.getAvailableAmount(), outBound.getAmount());
		Float totalAmount = CalculateUtils.sub(account.getTotalAmount(), outBound.getAmount());
		account.setFreezingAmount(freeAmount);
		account.setAvailableAmount(availableAmount);
		account.setTotalAmount(totalAmount);
		return account;
	}
	
	private Account getUserInfo (String type,String weixin) {
		Account account = null;
		if(StringUtils.equals(Account.ACCOUNT_TYPE_TRADER, type)) {
			Trader trader = traderRepository.findByWeixin(weixin);
			if (trader != null) {
				account = trader.getAccount();
			} 
		} else if(StringUtils.equals(Account.ACCOUNT_TYPE_COMPANY, type)) {
			Company company = companyRepository.findByWeixin(weixin);
			if (company != null) {
				account = company.getAccount();
			} 
		} else if(StringUtils.equals(Account.ACCOUNT_TYPE_INVESTOR, type)) {
			Investor investor = investorRepository.findByWeixin(weixin);
			if (investor != null) {
				account = investor.getAccount();
			} 
		}
		return account;
	}

	/**
	 * 插入出账记录表
	 * 
	 * @param parames
	 * @return
	 */
	private AccountOutBound saveOutBound(Map<String, String> parames) {
		AccountOutBound accountOutBound = new AccountOutBound();
		accountOutBound.setWeixin(parames.get("weixin"));
		accountOutBound.setAmount(Float.valueOf(parames.get("amount")));
		accountOutBound.setName(parames.get("name"));
		accountOutBound.setType(parames.get("userType"));
		accountOutBound.setState(accountOutBound.getState());
		accountOutBound.setCode(parames.get("identificationNumber"));
		String billingNumber = mahId + format.format(Calendar.getInstance().getTime());
		accountOutBound.setBillingNumber(billingNumber);
		accountOutBound.setCreater(parames.get("weixin"));
		accountOutBound.setRemoved(false);
		accountOutBound.setCashMode(AccountOutBound.ACCOUNT_OUT_BOUND_MODE_REDPACK);
		OutBoundRejectReason outBoundReject = new OutBoundRejectReason();
		outBoundReject = outBoundRejectReasonRepository.save(outBoundReject);
		accountOutBound.setOutBoundRejectReason(outBoundReject);
		//个人总账逻辑处理 可用余额 - 提现金额
		Account account = updateAmount(accountOutBound);
		accountOutBound.setAccount(account);
		try {
			accountOutboundRepository.saveAndFlush(accountOutBound);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("提现业务异常：",e.getMessage());
			return null;
		}
		return accountOutBound;
	}

	private boolean checkApplayPassword(String password,String userType,String weixin) {
		String tempPassword = getUserPassword(userType,weixin);
		password = DigestUtils.md5Hex(password);
		if (StringUtils.isEmpty(password) || !StringUtils.equals(password, tempPassword)) {
			return false;
		}
		return true;
	}

	private String getUserPassword(String userType,String weixin) {
		String password = null;
		if(StringUtils.equals(Account.ACCOUNT_TYPE_TRADER, userType)) {
			Trader trader = traderRepository.findByWeixin(weixin);
			if (trader != null) {
				password = trader.getAlipay();
			} 
		} else if(StringUtils.equals(Account.ACCOUNT_TYPE_COMPANY, userType)) {
			Company company = companyRepository.findByWeixin(weixin);
			if (company != null) {
				password = company.getAlipay();
			} 
		} else if(StringUtils.equals(Account.ACCOUNT_TYPE_INVESTOR, userType)) {
			Investor investor = investorRepository.findByWeixin(weixin);
			if (investor != null) {
				password = investor.getAlipay();
			} 
		}
		return password;
	}
}
