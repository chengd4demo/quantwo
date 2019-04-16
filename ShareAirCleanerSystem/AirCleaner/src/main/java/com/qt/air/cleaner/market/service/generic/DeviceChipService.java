package com.qt.air.cleaner.market.service.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;

public interface DeviceChipService {

	/**
	 * 设备滤芯监控分页查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<Device> findAllDevice(DeviceChipView deviceChipView, Pageable pageable);
	
}
