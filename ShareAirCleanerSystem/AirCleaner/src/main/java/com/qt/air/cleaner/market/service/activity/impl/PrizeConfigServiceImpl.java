package com.qt.air.cleaner.market.service.activity.impl;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.activity.PrizeConfig;
import com.qt.air.cleaner.market.domain.activity.PrizeItemConfig;
import com.qt.air.cleaner.market.domain.generic.Trader;
import com.qt.air.cleaner.market.repository.activity.PrizeConfigRepository;
import com.qt.air.cleaner.market.repository.activity.PrizeItemConfigRepository;
import com.qt.air.cleaner.market.repository.generic.TraderRepository;
import com.qt.air.cleaner.market.service.activity.PrizeConfigService;
import com.qt.air.cleaner.market.vo.activity.PrizeConfigView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class PrizeConfigServiceImpl implements PrizeConfigService{
	@Resource
	PrizeConfigRepository prizeConfigRepository;
	@Resource
	PrizeItemConfigRepository prizeItemConfigRepository;
	@Resource
	TraderRepository traderRepository;
	
	
	@Override
	public Page<PrizeConfig> findAllPrize(PrizeConfigView prizeConfigView, Pageable pageable) {
		Specification<PrizeConfig> specification = new Specification<PrizeConfig>() {
			@Override
			public Predicate toPredicate(Root<PrizeConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(!StringUtils.isEmpty(prizeConfigView.getPrizeItemConfigId())) {//奖项名称	
					Predicate p1 = cb.equal(root.get("prizeItemConfig").get("id"),prizeConfigView.getPrizeItemConfigId());
					conditions.add(p1);
				}
				String prizeName = prizeConfigView.getPrizeCategory();
				if (StringUtils.isNotBlank(prizeName)) { //奖品名称
					Predicate p2 = cb.like(root.get("prizeName"), "%" + prizeName + "%");
					conditions.add(p2);
				}
				if(!StringUtils.isEmpty(prizeConfigView.getTraderId())) {//所属商家	
					Predicate p3 = cb.equal(root.get("trader").get("id"),prizeConfigView.getTraderId());
					conditions.add(p3);
				}
				String prizeCategory = prizeConfigView.getPrizeCategory();
				if(StringUtils.isNotBlank(prizeCategory)) {//奖品品类
					Predicate p4 = cb.equal(root.get("prizeCategory"), prizeCategory);
					conditions.add(p4);
				}
				String state = prizeConfigView.getState();
				if(StringUtils.isNotBlank(state)) {//奖品类型
					Predicate p5 = cb.equal(root.get("state"), state);
					conditions.add(p5);
				}
				String effectiveTime = prizeConfigView.getEffectiveTime();
				if (StringUtils.isNotBlank(effectiveTime)) { // 有效期
					Predicate p6 = cb.like(root.get("effectiveTime"), StringUtils.trim(effectiveTime.replaceAll("-", "")) + "%");
					conditions.add(p6);
				}
				Predicate p7 = cb.equal(root.get("removed"), false);
				conditions.add(p7);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return prizeConfigRepository.findAll(specification, pageable);
	}
	
	/**
	 * 奖品设置修改查询
	 * @param id
	 * @return
	 */
	@Override
	public PrizeConfig findById(String id) {
		return prizeConfigRepository.findOne(id);
	}
	
	@Override
	public List<PrizeConfig> findAll(boolean removed) {
		return prizeConfigRepository.findByRemoved(false);
	}

	@Override
	public void saveOrUpdate(PrizeConfigView prizeConfigView) {
		if(prizeConfigView != null){
			PrizeConfig prizeConfig = null;
			String id = prizeConfigView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if(StringUtils.isNoneBlank(id)){
				prizeConfig = prizeConfigRepository.findOne(id);
				BeanUtils.copyProperties(prizeConfigView, prizeConfig);
				prizeConfig.setLastOperateTime(nowDate);
				prizeConfig.setLastOperator(principal.getUser().getUsername());
			}else {
				prizeConfig = new PrizeConfig();
				BeanUtils.copyProperties(prizeConfigView, prizeConfig);
				prizeConfig.setState(PrizeConfig.PRIZE_CONFIG_REFUND.toString());
				prizeConfig.setCreateTime(nowDate);
				prizeConfig.setCreater(principal.getUser().getUsername());
			}
			this.updateRelated(prizeConfig,prizeConfigView);
			prizeConfigRepository.saveAndFlush(prizeConfig);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
		
	}
	/**
	 * 更新关联数据表
	 * 
	 * @param prizeConfig
	 * @param prizeConfigView
	 * @return
	 * */
	private void updateRelated(PrizeConfig prizeConfig,PrizeConfigView prizeConfigView) {		
		String prizeItemConfigId = prizeConfigView.getPrizeItemConfigId();
		if (StringUtils.isNotBlank(prizeItemConfigId)) {
			PrizeItemConfig prizeItemConfig = prizeItemConfigRepository.findByIdAndRemoved(prizeItemConfigId, false);
			if (prizeItemConfigId != null) {
				prizeConfig.setPrizeItemConfig(prizeItemConfig);
			}
		}
		String traderId = prizeConfigView.getTraderId();
		if (StringUtils.isNotBlank(traderId)) {
			Trader trader = traderRepository.findByIdAndRemoved(traderId, false);
			if (trader != null) {
				prizeConfig.setTrader(trader);
			}
		}
	}

	/**
	 *奖品信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		prizeConfigRepository.deletePrizeConfig(id);
	}
	/**
	 * 奖品激活确认
	 * @param id
	 * 
	 * */ 
	@Override
	public void updateState(String id,String state) {
		if(StringUtils.isNoneBlank(id)) {
			prizeConfigRepository.updateState(id,state);
		}
		
	}

	
}
