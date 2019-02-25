package com.qt.air.cleaner.market.service.platform;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.vo.platform.PlatformSetView;

public interface PlatformSetService {
	List<ShareProfit> findAll();

	void save(List<ShareProfit> shareProfit);

	Page<ShareProfit> findAllSet(PlatformSetView platformSetView, Pageable pageable);

	long findCount(PlatformSetView platformSetView);

	ShareProfit findById(String id);

	boolean delete(String id);

	String findByPlatformId(String distributionRatio);
}
