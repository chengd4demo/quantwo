package com.qt.air.cleaner.market.service.platform.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.repository.platform.ShareProfitRepository;
import com.qt.air.cleaner.market.service.platform.PlatformSetService;

@Service
@Transactional
public class PlatformSetServiceImpl implements PlatformSetService {
	@Resource
	ShareProfitRepository shareProfitRepository;

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

}
