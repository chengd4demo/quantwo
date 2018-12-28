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

import org.apache.commons.lang.RandomStringUtils;
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
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;
import com.qt.air.cleaner.market.domain.generic.Investor;
import com.qt.air.cleaner.market.repository.generic.DeviceBatchRepository;
import com.qt.air.cleaner.market.repository.generic.InvestorRepository;
import com.qt.air.cleaner.market.vo.generic.DeviceBatchView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;

/**
 * 设备批次管理业务层
 * 
 * @author Jansan.Ma
 *
 */
@Service
@Transactional
public class DeviceBatchServiceImpl implements com.qt.air.cleaner.market.service.generic.DeviceBatchService {
	@Resource
	DeviceBatchRepository deviceBatchRepository;
	@Resource
	InvestorRepository investorRepository;
	
	/**
	 * 设备批次分页查询实现方法
	 * 
	 * @param batchNo
	 * @param batchName
	 * @param investorId
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<DeviceBatch> findAllDeviceBatch(String batchNo, String batchName, String investorId, Pageable pageable) {
		/**
		 * 多个字段排序，特殊处理
		 */
		Specification<DeviceBatch> specification = new Specification<DeviceBatch>() {
			@Override
			public Predicate toPredicate(Root<DeviceBatch> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(StringUtils.isNotBlank(investorId)){	//所属公司
					Predicate p1 = cb.equal(root.get("investor").get("id"), investorId);
					conditions.add(p1);
				}
				if(StringUtils.isNotBlank(batchName)) { //批次名称
					Predicate p2 = cb.like(root.get("batchName"), "%" + batchName + "%");
					conditions.add(p2);
				}
				if(StringUtils.isNotBlank(batchNo)) { //批次编号
					Predicate p3 = cb.like(root.get("batchNo"), "%" + batchNo);
					conditions.add(p3);
				}
				Predicate p4 = cb.equal(root.get("removed"), false);
				conditions.add(p4);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return deviceBatchRepository.findAll(specification, pageable);
	}

	/**
	 * 设备批次修改查询
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public DeviceBatch findById(String id) {
		return deviceBatchRepository.findOne(id);
	}
	
	/**
	 * 设备批次信息更新或保存
	 * 
	 * @param deviceBatchView
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	public void saveOrUpdate(DeviceBatchView deviceBatchView) throws BusinessException {
		if (deviceBatchView != null) {
			DeviceBatch deviceBatch = null;
			Investor investor = null;
			String id = deviceBatchView.getId();
			String investorId = deviceBatchView.getInvestorId();
			if (StringUtils.isNotBlank(investorId)) {
				investor = investorRepository.findOne(investorId);
			} else {
				throw new BusinessException(ErrorCodeEnum.ES_1002.getErrorCode(), ErrorCodeEnum.ES_1002.getMessage());
			}
			if (StringUtils.isNotBlank(id)) {
				deviceBatch = deviceBatchRepository.findOne(id);
				BeanUtils.copyProperties(deviceBatchView, deviceBatch);
				deviceBatch.setInvestor(investor);
			} else {
				deviceBatch = new DeviceBatch();
				Date nowDate = Calendar.getInstance().getTime();
				BeanUtils.copyProperties(deviceBatchView, deviceBatch);
				deviceBatch.setBatchNo(RandomStringUtils.randomNumeric(15));
				deviceBatch.setInvestor(investor);
				deviceBatch.setCreateTime(nowDate);
				deviceBatch.setLastOperateTime(nowDate);
				Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
				deviceBatch.setLastOperator(principal.getUser().getUsername());
				deviceBatch.setCreater(principal.getUser().getUsername());
			}
			deviceBatchRepository.saveAndFlush(deviceBatch);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
	}

	
	/**
	 *设备批次信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		deviceBatchRepository.deleteDeviceBatch(id);
	}
}
