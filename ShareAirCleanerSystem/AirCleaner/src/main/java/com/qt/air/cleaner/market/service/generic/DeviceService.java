package com.qt.air.cleaner.market.service.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.vo.generic.DeviceView;

public interface DeviceService {
	public Page<Device> findAllDevice(DeviceView deviceView, Pageable pageable);
	/**
	 * 设备信息修改查询
	 * @param id
	 * @return
	 */
	public Device findById(String id);
	/** 
	* @Title: saveOrUpdate 
	* @Description: 设备信息新增或更新 
	* @param @param companyView    设定文件 
	* @return void    返回类型 
	* @throws 
	*/ 
	public void saveOrUpdate(DeviceView deviceView) throws BusinessException;
	/** 
	* @Title: delete 
	* @Description: 设备信息删除
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	*/ 
	public void delete(String id);
	
	/**
	 * 生成二维码信息
	 * @param machNo
	 * @param string
	 * @return
	 */
	public String generateQRcode(String machNo, String string);
	
	/**
	 * 设备信息批量导入
	 * @param deviceBatchId
	 * @param name
	 * @param file
	 * @return
	 */
	public int batchImport(String deviceBatchId,String name, MultipartFile file);
}
