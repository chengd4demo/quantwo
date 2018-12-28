package com.qt.air.cleaner.market.service.activity.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.account.WinningConfing;
import com.qt.air.cleaner.market.repository.activity.WinningConfingRepository;
import com.qt.air.cleaner.market.service.activity.WinningConfingService;
import com.qt.air.cleaner.market.vo.activity.WinningConfingView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

@Service
public class WinningConfingServiceImpl implements WinningConfingService{
	@Resource
	WinningConfingRepository winningConfingRepository;
	
	@Override
	public Page<WinningConfing> findAllWinningConfing(WinningConfingView winningConfingView, Pageable pageable) {
		Specification<WinningConfing> specification = new Specification<WinningConfing>() {
			@Override
			public Predicate toPredicate(Root<WinningConfing> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String phone = winningConfingView.getPhone();
				if (StringUtils.isNotBlank(phone)) { // 手机号码
					Predicate p1 = cb.like(root.get("phone"), "%" + StringUtils.trim(phone) + "%");
					conditions.add(p1);
				}
				String prizeTime = winningConfingView.getPrizeTime();
				if(StringUtils.isNotBlank(prizeTime)) {//领奖时间
					Predicate p2 = cb.like(root.get("prizeTime"),StringUtils.trim(prizeTime) +"%");
					conditions.add(p2);
				}
				String prizeName = winningConfingView.getPrizeName();
				if(StringUtils.isNotBlank(prizeName)) {//奖品名称
					Predicate p3 = cb.like(root.get("prizeName"),"%"+StringUtils.trim(prizeName)+"%");
					conditions.add(p3);
				}
				Integer state = winningConfingView.getState();
				if(state != null) {//是否兑奖
					Predicate p4 = cb.equal(root.get("state"), state);
					conditions.add(p4);
				}
				Predicate p5 = cb.equal(root.get("removed"), false);
				conditions.add(p5);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return winningConfingRepository.findAll(specification, pageable);
	}
	
	
	
	/**
	 * 发货确认
	 * @param winningConfing
	 * 
	 * */
	@Override
	public void saveOrUpdate(WinningConfingView winninggConfingView) throws BusinessException {
		if(winninggConfingView != null){
			WinningConfing winninggConfing = null;
			String id = winninggConfingView.getId();
			String logisticsNum = winninggConfingView.getLogisticsNum();	
			if (StringUtils.isNotBlank(id)) {
				winninggConfing = winningConfingRepository.findByIdAndRemoved(id, false);
				winninggConfing.setLogisticsNum(logisticsNum);
				winningConfingRepository.saveAndFlush(winninggConfing);
			} else {
				throw new BusinessException(ErrorCodeEnum.ES_9011.getErrorCode(), ErrorCodeEnum.ES_9011.getMessage());
			}
		}
		
	}



	@Override
	public WinningConfing findById(String id) {
		return winningConfingRepository.findOne(id);
		
	}

}
