package com.qt.air.cleaner.market.service.activity.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.qt.air.cleaner.market.service.activity.ActivitySetService;
import com.qt.air.cleaner.system.repository.config.ConfigRepository;

@Service
@Transactional
public class ActivitySetServiceImpl implements ActivitySetService {
	@Resource
	ConfigRepository configRepository;
	@Override
	public void updateConfig(Boolean on, String bussinessId) {
		configRepository.updateConfig(on, bussinessId);
	}

}
