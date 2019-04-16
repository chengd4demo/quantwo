package com.qt.air.cleaner.market.service.generic.impl;

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
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.repository.generic.CompanyRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceBatchRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceChipRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceRepository;
import com.qt.air.cleaner.market.repository.generic.InvestorRepository;
import com.qt.air.cleaner.market.repository.generic.SalerRepository;
import com.qt.air.cleaner.market.repository.generic.TraderRepository;
import com.qt.air.cleaner.market.repository.price.PriceSystemRepository;
import com.qt.air.cleaner.market.service.generic.DeviceChipService;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;

@Service
@Transactional
public class DeviceChipServiceImpl implements DeviceChipService{

	@Resource
	DeviceChipRepository deviceChipRepository;
	@Resource
	DeviceRepository deviceRepository;
	@Resource
	InvestorRepository investorRepository;
	@Resource
	PriceSystemRepository priceSystemRepository;
	@Resource
	CompanyRepository companyRepository;
	@Resource
	TraderRepository traderRepository;
	@Resource
	SalerRepository salerRepository;
	@Resource
	DeviceBatchRepository deviceBatchRepository;
	
	@Override
	public Page<Device> findAllDevice(DeviceChipView deviceChipView, Pageable pageable) {
		Specification<Device> specification = new Specification<Device>() {
			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(!StringUtils.isEmpty(deviceChipView.getMachNo())) {//设备编号
					Predicate p1 = cb.like(root.get("machNo"),"%"+deviceChipView.getMachNo());
					conditions.add(p1);
				}
				if(!StringUtils.isEmpty(deviceChipView.getDeviceSequence())) {//设序列号
					Predicate p2 = cb.like(root.get("deviceSequence"),"%"+deviceChipView.getDeviceSequence());
					conditions.add(p2);
				}
				if(!StringUtils.isEmpty(deviceChipView.getInvestorId())) {//投资商	
					Predicate p3 = cb.equal(root.get("investor").get("id"), deviceChipView.getInvestorId());
					conditions.add(p3);
				}
				if(!StringUtils.isEmpty(deviceChipView.getTraderId())) {//商家	
					Predicate p4 = cb.equal(root.get("trader").get("id"),deviceChipView.getTraderId());
					conditions.add(p4);
				}
				if(!StringUtils.isEmpty(deviceChipView.getSalerId())) {//促销员	
					Predicate p5 = cb.equal(root.get("saler").get("id"),deviceChipView.getSalerId());
					conditions.add(p5);
				}
				/*Predicate p6 = cb.equal(root.get("deviceBatch").get("id"), deviceChipView.getDeviceBatchId());
				conditions.add(p6);*/
				
				Predicate p7 = cb.equal(root.get("removed"),false);
				conditions.add(p7);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return deviceChipRepository.findAll(specification,pageable);
	}

}
