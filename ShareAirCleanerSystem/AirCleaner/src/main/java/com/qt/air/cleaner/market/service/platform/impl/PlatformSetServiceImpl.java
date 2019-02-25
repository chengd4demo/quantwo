package com.qt.air.cleaner.market.service.platform.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.qt.air.cleaner.market.domain.account.Account;
import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.repository.generic.DeviceRepository;
import com.qt.air.cleaner.market.repository.platform.ShareProfitRepository;
import com.qt.air.cleaner.market.service.platform.PlatformSetService;
import com.qt.air.cleaner.market.vo.platform.PlatformSetView;

@Service
@Transactional
public class PlatformSetServiceImpl implements PlatformSetService {
	@Resource
	ShareProfitRepository shareProfitRepository;
	@Resource
	DeviceRepository deviceRepository;

	@Override
	public List<ShareProfit> findAll() {
		return shareProfitRepository.findAll();
	}

	@Override
	public void save(List<ShareProfit> shareProfit) {
		List<ShareProfit> result = new ArrayList<>();
		if(!CollectionUtils.isEmpty(shareProfit)) {
			shareProfit.forEach(profit -> {
				ShareProfit profitTemp = shareProfitRepository.findByType(profit.getType());
				if(profitTemp == null) {
					result.add(profit);
				}else if(profitTemp != null && ((Math.abs(profitTemp.getFree()-profit.getFree())!=0.0f) || (Math.abs(profitTemp.getScale()-profit.getScale())!=0.0f))) {
					result.add(profit);
					shareProfitRepository.delete(profitTemp);
				}
			});
			if(!CollectionUtils.isEmpty(result)) {
				Float free = result.get(0).getFree();
				 //更新所有已设定的手续费，手续费统一一样
				shareProfitRepository.updateAllFree(free);
				//保存方法
				shareProfitRepository.save(result);
			}
			
		}
	}

	@Override
	public Page<ShareProfit> findAllSet(PlatformSetView platformSetView, Pageable pageable) {
		Specification<ShareProfit> specification = new Specification<ShareProfit>() {
			@Override
			public Predicate toPredicate(Root<ShareProfit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = platformSetView.getName();
				if (StringUtils.isNotBlank(name)) { // 订单号
					Predicate p1 = cb.like(root.get("name"), "%" + StringUtils.trim(name) + "%");
					conditions.add(p1);
				}
				String pid = platformSetView.getPid();
				if(StringUtils.isNotBlank( pid)) {
					Predicate p2 = cb.equal(root.get("pid"), pid);
					conditions.add(p2);
				}
				String type = platformSetView.getType();
				if(StringUtils.isNotBlank( type)) {
					Predicate p3 = cb.like(root.get("type"), "%" + StringUtils.trim(type) + "%");
					conditions.add(p3);
				}
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}

		};
		return shareProfitRepository.findAll(specification, pageable);
	}

	@Override
	public long findCount(PlatformSetView platformSetView) {
		ShareProfit platform = new ShareProfit();
		if(StringUtils.isNotBlank(platformSetView.getName())) {
			platform.setName(platformSetView.getName());
		}
		if(StringUtils.isNotBlank(platformSetView.getPid())) {
			platform.setPid(platformSetView.getPid());
		}
		if(StringUtils.isNotBlank(platformSetView.getType())) {
			platform.setType(platformSetView.getType());
		}
		Example<ShareProfit> example = Example.of(platform);
		return shareProfitRepository.count(example);
	}

	/**
	 * 分润信息查询
	 * 
	 * @param id
	 */
	@Override
	public ShareProfit findById(String id) {
		return shareProfitRepository.findById(id);
	}

	/**
	 * 分润信息删除
	 * 
	 * @param id
	 */
	@Override
	public boolean delete(String id) {
		Device device = deviceRepository.findByDistributionRatioAndRemoved(id, Boolean.FALSE);
		if(device == null) {
			List<ShareProfit> platformSetFiledList = shareProfitRepository.findByPlatformSet(id);
			if(!CollectionUtils.isEmpty(platformSetFiledList) && platformSetFiledList.size()>1) {
				//删除下面所有子数据
				shareProfitRepository.deleteInBatch(platformSetFiledList);
			} else {
				shareProfitRepository.delete(id);
			}
			return true;
		} 
		return false;
		
	}

	@Override
	public String findByPlatformId(String distributionRatio) {
		List<ShareProfit> platformSetFiledList = shareProfitRepository.findByPlatformSet(distributionRatio);
		String result = "";
		if(CollectionUtils.isEmpty(platformSetFiledList)) {
			return result;
		} else {
			String name = "";
			String type = "";
			Float scale = 0.00f;
			Float free = 0.00f;
			for(ShareProfit platformSet: platformSetFiledList) {
				name = platformSet.getName();
				type = platformSet.getType();
				scale = platformSet.getScale();
				free = platformSet.getFree();
				if(StringUtils.equals(Account.ACCOUNT_TYPE_COMPANY, type)) {
					result += name + ":" + scale + "% ";
				} else if(StringUtils.equals(Account.ACCOUNT_TYPE_AGENT_ZD, type)) {
					result += name + ":" + scale + "% ";
				}  else if(StringUtils.equals(Account.ACCOUNT_TYPE_AGENT_DL, type)) {
					result += name + ":" + scale + "% ";
				} else if(StringUtils.equals(Account.ACCOUNT_TYPE_INVESTOR, type)) {
					result += name + ":" + scale + "% (耗材费："+ free +"元/h) ";
				} else if(StringUtils.equals(Account.ACCOUNT_TYPE_TRADER, type)) {
					result += name + ":" + scale + "% ";
				}  else if(StringUtils.equals(Account.ACCOUNT_TYPE_SALER, type)) {
					result += name + ":" + scale + "% ";
				}
			}
		}
		return result;
	}

}
