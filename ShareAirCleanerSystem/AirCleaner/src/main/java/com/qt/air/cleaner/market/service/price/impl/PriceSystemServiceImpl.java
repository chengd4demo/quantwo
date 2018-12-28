package com.qt.air.cleaner.market.service.price.impl;

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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.price.PriceModel;
import com.qt.air.cleaner.market.domain.price.PriceSystem;
import com.qt.air.cleaner.market.repository.generic.InvestorRepository;
import com.qt.air.cleaner.market.repository.price.PriceModelRepository;
import com.qt.air.cleaner.market.repository.price.PriceSystemRepository;
import com.qt.air.cleaner.market.service.price.PriceSystemService;
import com.qt.air.cleaner.market.vo.price.PriceSystemView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
@Transactional
public class PriceSystemServiceImpl implements PriceSystemService{

	@Resource
	PriceSystemRepository priceSystemRepository;
	@Resource
	InvestorRepository investorRepository;
	@Resource
	PriceModelRepository priceModelRepository;
	
	@Override
	public List<PriceSystem> findAll(boolean removed) {
		return priceSystemRepository.findByRemovedAndState(false, PriceSystem.PRICE_SYSTEM_CONFIRM);
	}
	/**
	 * 价格体系管理查询实现方法
	 * 
	 * @author SongXueShuang
	 *
	 */
	@Override
	public Page<PriceSystem> findAllPriceSystem(PriceSystemView priceSystemView, Pageable pageable) {
		Specification<PriceSystem> specification = new Specification<PriceSystem>() {
			@Override
			public Predicate toPredicate(Root<PriceSystem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(!StringUtils.isEmpty(priceSystemView.getName())) {//价格体系名称
					Predicate p1 = cb.like(root.get("name"),"%"+priceSystemView.getName()+"%");
					conditions.add(p1);
				}
				
				Predicate p2 = cb.equal(root.get("removed"),false);
				conditions.add(p2);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return priceSystemRepository.findAll(specification,pageable);
	}

	
	/**
	 * 价格体系管理修改查询
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public PriceSystem findById(String id) {
		return priceSystemRepository.findOne(id);
	}
	
	@Override
	public void saveOrUpdate(PriceSystemView priceSystemView) {
		if (priceSystemView != null) {
			PriceSystem priceSystem = null;
			String id = priceSystemView.getId();
			if (StringUtils.isNotBlank(id)) {
				priceSystem = priceSystemRepository.findOne(id);
				BeanUtils.copyProperties(priceSystemView, priceSystem);
				PriceModel priceModel = priceModelRepository.findByIdAndRemoved(priceSystem.getPriceModeId(), false);
				if(null != priceModel) {
					priceSystem.setActiveModel(priceModel);
				}
			} else {
				priceSystem = new PriceSystem();
				Date nowDate = Calendar.getInstance().getTime();
				BeanUtils.copyProperties(priceSystemView, priceSystem);
				priceSystem.setCreateTime(nowDate);
				priceSystem.setLastOperateTime(nowDate);
				Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
				priceSystem.setLastOperator(principal.getUser().getUsername());
				priceSystem.setCreater(principal.getUser().getUsername());
			}
			this.updateRelated(priceSystem,priceSystemView);
			priceSystemRepository.saveAndFlush(priceSystem);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
	}
	/**
	 * 更新关联数据表
	 * 
	 * @param priceSystem
	 * @param priceSystemView
	 * @return
	 * */
	private void updateRelated(PriceSystem priceSystem,PriceSystemView priceSystemView) {		
		String priceModelId = priceSystemView.getPriceModelId();
		if (StringUtils.isNotBlank(priceModelId)) {
			PriceModel priceModel = priceModelRepository.findByIdAndRemoved(priceModelId, false);
			if (priceModel != null) {
				priceSystem.setActiveModel(priceModel);
			}
		}
	}
	
	/**
	 *价格体系管理删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		priceSystemRepository.deletePriceSystem(id);
	}
	
	/**
	 * 价格体系激活确认
	 * @param id
	 * 
	 * */ 
	@Override
	public void updateState(String id, Integer state) {
		if(StringUtils.isNoneBlank(id)) {
			priceSystemRepository.updateState(id,state);
		}
		
	}

}
