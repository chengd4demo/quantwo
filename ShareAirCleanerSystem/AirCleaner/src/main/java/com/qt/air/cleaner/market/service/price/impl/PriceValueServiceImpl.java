package com.qt.air.cleaner.market.service.price.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.qt.air.cleaner.market.domain.price.PriceValue;
import com.qt.air.cleaner.market.repository.price.PriceModelRepository;
import com.qt.air.cleaner.market.repository.price.PriceValueRepository;
import com.qt.air.cleaner.market.service.price.PriceValueService;
import com.qt.air.cleaner.market.vo.price.PriceValueView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

/**
 * 
 * 指定模型价格业务层
 * 
 */
@Service
public class PriceValueServiceImpl implements PriceValueService{
	@Resource
	PriceValueRepository priceValueRepository;
	@Resource
	PriceModelRepository priceModelRepository;
	/**	
	 * 指定模型价格分页查询实现方法
	 * @return
	 */
	@Override
	public Page<PriceValue> findAllPriceValue(PriceValueView priceValueView,Pageable pageable) {
		Specification<PriceValue> specification = new Specification<PriceValue>() {
			@Override
			public Predicate toPredicate(Root<PriceValue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();	
				Predicate p1 = cb.equal(root.get("priceModel").get("id"),priceValueView.getModelId());
				conditions.add(p1);
				Predicate p2 = cb.equal(root.get("removed"),false);
				conditions.add(p2);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return priceValueRepository.findAll(specification,pageable);
	}
	
	/**
	 * 指定模型价格修改查询
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public PriceValue findById(String id) {	
		return priceValueRepository.findOne(id);
	}
	
	/**
	 *指定模型价格更新或保存
	 * 
	 * @param priceValueView
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public void saveOrUpdate(PriceValueView priceValueView) throws BusinessException {		
		if (priceValueView != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			PriceValue priceValue = null;	
			String id = priceValueView.getId();	
			String modelId = priceValueView.getModelId();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			Date nowDate = Calendar.getInstance().getTime();				
			if (StringUtils.isNotBlank(id)) {
				priceValue = priceValueRepository.findByIdAndRemoved(id, false);
				BeanUtils.copyProperties(priceValueView, priceValue);	
				BigDecimal bigDeciaml = new BigDecimal((float)priceValueView.getDiscount()/100);
				priceValue.setDiscount(bigDeciaml.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				priceValue.setLastOperator(principal.getUser().getUsername());
				priceValue.setLastOperateTime(nowDate);
			} else {
				priceValue = new PriceValue();
				BeanUtils.copyProperties(priceValueView, priceValue);	
				try {
					PriceModel priceModel = priceModelRepository.findByIdAndRemoved(modelId, false);
					priceValue.setPriceModel(priceModel);
					priceValue.setActiveStartTime(df.parse(priceValueView.getActiveStartTime()));
					priceValue.setActiveEndTime(df.parse(priceValueView.getActiveEndTime()));
					BigDecimal bigDeciaml = new BigDecimal((float)priceValueView.getDiscount()/100);
					priceValue.setDiscount(bigDeciaml.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				priceValue.setCreater(principal.getUser().getUsername());
				priceValue.setCreateTime(nowDate);
			}
			priceValueRepository.saveAndFlush(priceValue);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
	
	}
	
	@Override
	public void delete(String id) {
		PriceValue priceValue = priceValueRepository.findOne(id);
		priceValue.setRemoved(Boolean.TRUE);
		priceValueRepository.saveAndFlush(priceValue);
		
	}
	
}
