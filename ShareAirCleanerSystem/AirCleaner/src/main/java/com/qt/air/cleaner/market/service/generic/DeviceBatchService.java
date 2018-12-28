package com.qt.air.cleaner.market.service.generic;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;
import com.qt.air.cleaner.market.vo.generic.DeviceBatchView;

/**
 * 设备批次管理
 * 
 * @author Jansan.Ma
 *
 */
public interface DeviceBatchService {
	/**
	 * 设备批次分页查询
	 * 
	 * @param batchNo
	 * @param deviceName
	 * @param investorId
	 * @param pageable
	 * @return
	 */
	public Page<DeviceBatch> findAllDeviceBatch(String batchNo, String deviceName, String investorId, Pageable pageable);
	
	/**
	 * 设备批次修改查询
	 * @param id
	 * @return
	 */
	public DeviceBatch findById(String id);
	
	/**
	 * 设备批次信息更新或保存
	 * 
	 * @param deviceBatchView
	 * @throws BusinessException
	 */
	public void saveOrUpdate(DeviceBatchView deviceBatchView) throws BusinessException;
	
	/**
	 * 设备批次信息删除
	 * 
	 * @param id
	 */
	public void delete(String id);
}
