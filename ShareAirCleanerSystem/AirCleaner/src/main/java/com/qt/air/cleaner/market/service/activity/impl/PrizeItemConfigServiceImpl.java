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
import com.qt.air.cleaner.market.domain.activity.PrizeItemConfig;
import com.qt.air.cleaner.market.repository.activity.PrizeConfigRepository;
import com.qt.air.cleaner.market.repository.activity.PrizeItemConfigRepository;
import com.qt.air.cleaner.market.repository.generic.TraderRepository;
import com.qt.air.cleaner.market.service.activity.PrizeItemConfigService;
import com.qt.air.cleaner.market.vo.activity.PrizeItemConfigView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class PrizeItemConfigServiceImpl implements PrizeItemConfigService{
	@Resource
	PrizeItemConfigRepository prizeItemConfigRepository;
	@Resource
	PrizeConfigRepository prizeConfigRepository;
	@Resource
	TraderRepository traderRepository;
	@Override
	public Page<PrizeItemConfig> findAllPrizeItem(PrizeItemConfigView prizeItemConfigView, Pageable pageable) {
		Specification<PrizeItemConfig> specification = new Specification<PrizeItemConfig>() {
			@Override
			public Predicate toPredicate(Root<PrizeItemConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = prizeItemConfigView.getName();
				if (StringUtils.isNotBlank(name)) { // 奖项名称
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}
				Predicate p3 = cb.equal(root.get("removed"), false);
				conditions.add(p3);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return prizeItemConfigRepository.findAll(specification, pageable);
	}
	
	/**
	 * 奖项设置修改查询
	 * @param id
	 * @return
	 */
	@Override
	public PrizeItemConfig findById(String id) {
		return prizeItemConfigRepository.findOne(id);
	}
	
	@Override
	public List<PrizeItemConfig> findAll(boolean removed) {
		return prizeItemConfigRepository.findByRemoved(false);
	}

	@Override
	public void saveOrUpdate(PrizeItemConfigView prizeItemConfigView) {
		if(prizeItemConfigView != null){
			PrizeItemConfig prizeItemConfig = null;
			String id = prizeItemConfigView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if(StringUtils.isNoneBlank(id)){
				prizeItemConfig = prizeItemConfigRepository.findOne(id);
				BeanUtils.copyProperties(prizeItemConfigView, prizeItemConfig);
			}else {
				prizeItemConfig = new PrizeItemConfig();
				BeanUtils.copyProperties(prizeItemConfigView, prizeItemConfig);
				prizeItemConfig.setCreateTime(nowDate);
				prizeItemConfig.setCreater(principal.getUser().getUsername());
				prizeItemConfig.setLastOperateTime(nowDate);
				prizeItemConfig.setLastOperator(principal.getUser().getUsername());		
			}
			prizeItemConfigRepository.saveAndFlush(prizeItemConfig);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
		
	}

	/**
	 *奖项信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		prizeItemConfigRepository.deletePrizeItemConfig(id);
		
	}
	
}
