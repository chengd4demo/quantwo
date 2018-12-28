package com.qt.air.cleaner.market.service.platform;

import java.util.List;

import com.qt.air.cleaner.market.domain.platform.ShareProfit;

public interface PlatformSetService {
	List<ShareProfit> findAll();

	void save(List<ShareProfit> shareProfit);
}
