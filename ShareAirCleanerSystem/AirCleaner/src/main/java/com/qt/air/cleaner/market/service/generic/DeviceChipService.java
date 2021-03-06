package com.qt.air.cleaner.market.service.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.DeviceChip;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;

public interface DeviceChipService {

	/**
	 * 设备滤芯监控分页查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DeviceChip> findAllDevice(DeviceChipView deviceChipView, Pageable pageable);

	/**
	 * 滤芯时间恢复
	 * @param id
	 */
	public void updateDeviceChip(String id);
	
}
