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
import com.qt.air.cleaner.market.repository.price.PriceModelRepository;
import com.qt.air.cleaner.market.service.price.PriceModelService;
import com.qt.air.cleaner.market.vo.price.PriceModelView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

/**
 * 
 * 设备批次管理业务层
 * 
 */
@Service
public class PriceModelServiceImpl implements PriceModelService{
	@Resource
	PriceModelRepository priceModelRepository;
	/**
	 * 价格模型分页查询实现方法
	 * 
	 * @param name
	 * @param priceModelView
	 * @param description
	 * @param pageable
	 * @return
	 */

	@Override
	public Page<PriceModel> findAllPriceModel(PriceModelView priceModelView,Pageable pageable) {
		Specification<PriceModel> specification = new Specification<PriceModel>() {
			@Override
			public Predicate toPredicate(Root<PriceModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(!StringUtils.isEmpty(priceModelView.getName())) {//模型名称
					Predicate p1 = cb.like(root.get("name"),"%"+priceModelView.getName()+"%");
					conditions.add(p1);
				}
				Predicate p2 = cb.equal(root.get("removed"),false);
				conditions.add(p2);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return priceModelRepository.findAll(specification,pageable);
	}
	
	/**
	 * 价格模型修改查询
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public PriceModel findById(String id) {	
		return priceModelRepository.findOne(id);
	}
	
	/**
	 * 价格模型更新或保存
	 * 
	 * @param priceModelView
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public void saveOrUpdate(PriceModelView priceModelView) throws BusinessException {		
		if (priceModelView != null) {
			PriceModel priceMode = null;	
			String id = priceModelView.getId();			
			if (StringUtils.isNotBlank(id)) {
				priceMode = priceModelRepository.findOne(id);
				BeanUtils.copyProperties(priceModelView, priceMode);				
			} else {
				priceMode = new PriceModel();
				Date nowDate = Calendar.getInstance().getTime();				
				BeanUtils.copyProperties(priceModelView, priceMode);			
				priceMode.setCreateTime(nowDate);
				priceMode.setLastOperateTime(nowDate);
				Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
				priceMode.setLastOperator(principal.getUser().getUsername());
				priceMode.setCreater(principal.getUser().getUsername());
			}
			priceModelRepository.saveAndFlush(priceMode);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
	
	}

	@Override
	public void delete(String id) {
		PriceModel priceModel = priceModelRepository.findOne(id);
		priceModel.setRemoved(Boolean.TRUE);
		priceModelRepository.saveAndFlush(priceModel);
		//差批量删除
		
	}

	@Override
	public List<PriceModel> findAll() {		
		return priceModelRepository.findByRemoved(false);
	}


}
