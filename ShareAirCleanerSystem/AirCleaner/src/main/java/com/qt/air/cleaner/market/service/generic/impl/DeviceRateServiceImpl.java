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
import com.qt.air.cleaner.market.repository.generic.DeviceRateRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceRepository;
import com.qt.air.cleaner.market.repository.generic.InvestorRepository;
import com.qt.air.cleaner.market.repository.generic.SalerRepository;
import com.qt.air.cleaner.market.repository.generic.TraderRepository;
import com.qt.air.cleaner.market.repository.price.PriceSystemRepository;
import com.qt.air.cleaner.market.service.generic.DeviceRateService;
import com.qt.air.cleaner.market.vo.generic.DeviceRateView;

@Service
@Transactional
public class DeviceRateServiceImpl implements DeviceRateService{
	
	@Resource
	DeviceRateRepository deviceRateRepository;
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
	public Page<Device> findAllDevice(DeviceRateView deviceRateView, Pageable pageable) {
		Specification<Device> specification = new Specification<Device>() {
			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(!StringUtils.isEmpty(deviceRateView.getMachNo())) {//设备编号
					Predicate p1 = cb.like(root.get("machNo"),"%"+deviceRateView.getMachNo());
					conditions.add(p1);
				}
				if(!StringUtils.isEmpty(deviceRateView.getDeviceSequence())) {//设序列号
					Predicate p2 = cb.like(root.get("deviceSequence"),"%"+deviceRateView.getDeviceSequence());
					conditions.add(p2);
				}
				Predicate p3 = cb.equal(root.get("removed"), false);
				conditions.add(p3);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
			
		};
		return deviceRateRepository.findAll(specification,pageable);
	}

	
}
