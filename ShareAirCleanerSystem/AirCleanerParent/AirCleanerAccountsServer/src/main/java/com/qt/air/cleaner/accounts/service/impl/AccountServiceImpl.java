package com.qt.air.cleaner.accounts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.accounts.domain.Company;
import com.qt.air.cleaner.accounts.domain.Investor;
import com.qt.air.cleaner.accounts.domain.Trader;
import com.qt.air.cleaner.accounts.repository.CompanyService;
import com.qt.air.cleaner.accounts.repository.InvestorService;
import com.qt.air.cleaner.accounts.repository.TraderService;
import com.qt.air.cleaner.accounts.service.AccountService;
import com.qt.air.cleaner.accounts.vo.AccountDto;
import com.qt.air.cleaner.accounts.vo.AccountOutboundDto;
import com.qt.air.cleaner.base.contant.CommonConst;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.enums.AccountOutBoundEnum;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;



@RestController
@Transactional
public class AccountServiceImpl implements AccountService {
	private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Resource
	private TraderService traderService;
	@Resource
	private InvestorService investorService;
	@Resource
	private CompanyService companyService;
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	@Override
	public ResultInfo queryAccountDetailByWeixin(@PathVariable String weixin) {
		logger.info("execute method queryAccountDetailByWeixin() param --> weixin:{}", weixin);
		Map<String,Float> result = new HashMap<String,Float>();
		List<AccountDto> dtoes = new ArrayList<>(3);
		Trader trader = traderService.findByWeixin(weixin);
		if(trader != null) {
			dtoes.add(new AccountDto(trader, trader.getAccount()));
		}
		Investor investor = investorService.findByWeixin(weixin);
		if(investor != null) {
			dtoes.add(new AccountDto(investor, investor.getAccount()));
		}
		Company company = companyService.findByWeixin(weixin);
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
			sql.append("select createdate,amount,state");
			sql.append(" from (select row_.*, rownum rownum_");
			sql.append("	          from (select to_char(t.create_time,'yyyy-mm-dd hh24:mi:ss') createdate,");
			sql.append("                       t.amount,");
			sql.append("                       case");
			sql.append("                         when （t.state = 0 or t.state = 4 or t.state = -1） and (t.removed != 'Y') then '处理中'");
			sql.append("                         when t.state = 5 then  '未领取'");
			sql.append("                         when t.state = 1 then  '已完成'");
			sql.append("                         when t.removed = 'Y' then '已取消'");
			sql.append("                         else '其它' end state");
			sql.append("                    from ACT_ACCOUNT_OUTBOUND t");
			sql.append("                       where t.weixin = :weixin");
			if (AccountOutBoundEnum.CANCELLED.getStatus() == state){
				sql.append("                       and t.removed = :removed");
			} 
			if (AccountOutBoundEnum.REQUEST.getStatus() == state || AccountOutBoundEnum.UNCOLLECTED.getStatus() == state
					|| AccountOutBoundEnum.COMPLETED.getStatus() == state || AccountOutBoundEnum.NOTPASS.getStatus() == state) {
				sql.append("                       and t.state in (:state)");
			}
			sql.append("                	order by t.create_time desc) row_");
			sql.append("        where rownum <= :end )");
			sql.append("where rownum_ > :start");
			EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sql.toString())
					.addScalar("createdate",StandardBasicTypes.STRING)
					.addScalar("amount",StandardBasicTypes.FLOAT)
					.addScalar("state",StandardBasicTypes.STRING);
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

}
