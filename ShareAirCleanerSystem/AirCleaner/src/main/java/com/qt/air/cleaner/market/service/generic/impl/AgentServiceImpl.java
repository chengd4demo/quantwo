package com.qt.air.cleaner.market.service.generic.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.account.Account;
import com.qt.air.cleaner.market.domain.generic.Agent;
import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.repository.account.AccountRepository;
import com.qt.air.cleaner.market.repository.generic.AgentRepository;
import com.qt.air.cleaner.market.repository.platform.ShareProfitRepository;
import com.qt.air.cleaner.market.service.generic.AgentService;
import com.qt.air.cleaner.market.vo.generic.AgentView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {
	@Resource
	AgentRepository agentRepository;
	@Resource
	AccountRepository accountRepository;
	@Resource
	ShareProfitRepository shareProfitRepository;

	@Override
	public List<Agent> findAll(String type) {
		return agentRepository.findByTypeAndRemoved(type,false);
	}
	
	/**
	 * 代理商分页模糊查询
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Agent> findAllAgent(AgentView agentView, Pageable pageable) {
		Specification<Agent> specification = new Specification<Agent>() {
			@Override
			public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = agentView.getName();
				if (StringUtils.isNotBlank(name)) { // 代理商名称
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}				
				String phoneNumber = agentView.getPhoneNumber();
				if (StringUtils.isNotBlank(phoneNumber)) { // 联系电话
					Predicate p2 = cb.like(root.get("phoneNumber"), "%" + StringUtils.trim(phoneNumber) + "%");
					conditions.add(p2);
				}
				String type = agentView.getType();
				if(StringUtils.isNotBlank( type)) { //类型
					Predicate p3 = cb.equal(root.get("type"), StringUtils.trim(type));
					conditions.add(p3);
				}
				Predicate p4 = cb.equal(root.get("removed"), false);
				conditions.add(p4);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}			
		};
		return agentRepository.findAll(specification, pageable);
	}

	/**
	 * 代理商查询
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Agent findById(String id) {
		return agentRepository.findByIdAndRemoved(id, false);
	}
	
	/**
	 * 代理商新增或修改
	 * 
	 * @param agentView
	 */
	@Override
	public void saveOrUpdate(AgentView agentView) {
		if (agentView != null) {
			Agent agent = null;
			String id = agentView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isNoneBlank(id)) {
				agent = agentRepository.findByIdAndRemoved(id, false);
				if (agent != null) {
					BeanUtils.copyProperties(agentView, agent);
					agent.setLastOperator(principal.getUser().getUsername());
					agent.setLastOperateTime(nowDate);
				} else {
					throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(),ErrorCodeEnum.ES_1001.getMessage());
				}
			} else {
				agent = new Agent();
				BeanUtils.copyProperties(agentView, agent);
				agent.setCreateTime(nowDate);
				agent.setCreater(principal.getUser().getUsername());
				agent.setLastOperateTime(nowDate);
				agent.setLastOperator(principal.getUser().getUsername());
				Account account = new Account();
				account.setAccountType(Account.ACCOUNT_TYPE_COMPANY);
				account.setCreateTime(nowDate);
				account.setCreater(principal.getUser().getUsername());
				account =  accountRepository.saveAndFlush(account);
				agent.setAccount(account);
			}
			agentRepository.saveAndFlush(agent);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}		
	}

	/**
	 * 代理商信息删除
	 * 
	 * @param id
	 */
	@Override
	public boolean delete(String id) {
		ShareProfit shareProfit = new ShareProfit();
		shareProfit.setAgentId(id);
		Example<ShareProfit> example = Example.of(shareProfit);
		if(shareProfitRepository.exists(example)){
			return false;
		} else {
			Agent agent = agentRepository.findByIdAndRemoved(id, false);
			agentRepository.deleteAgent(id);
			accountRepository.deleteAccount(agent.getAccount().getId());
			return true;
		}
	}

}
