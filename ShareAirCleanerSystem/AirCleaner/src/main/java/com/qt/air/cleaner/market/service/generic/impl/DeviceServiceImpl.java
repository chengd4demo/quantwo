package com.qt.air.cleaner.market.service.generic.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.generic.Company;
import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.domain.generic.DeviceBatch;
import com.qt.air.cleaner.market.domain.generic.Investor;
import com.qt.air.cleaner.market.domain.generic.Saler;
import com.qt.air.cleaner.market.domain.generic.Trader;
import com.qt.air.cleaner.market.domain.price.PriceSystem;
import com.qt.air.cleaner.market.repository.generic.CompanyRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceBatchRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceRepository;
import com.qt.air.cleaner.market.repository.generic.InvestorRepository;
import com.qt.air.cleaner.market.repository.generic.SalerRepository;
import com.qt.air.cleaner.market.repository.generic.TraderRepository;
import com.qt.air.cleaner.market.repository.price.PriceSystemRepository;
import com.qt.air.cleaner.market.service.generic.DeviceService;
import com.qt.air.cleaner.market.vo.generic.DeviceExcel;
import com.qt.air.cleaner.market.vo.generic.DeviceView;
import com.qt.air.cleaner.utils.FileUtil;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.singalrain.framework.util.CollectionUtils;
import com.singalrain.framework.util.QRCodeUtil;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService{
	private final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
	@Resource
	DeviceRepository deviceRepository;
	@Resource
	InvestorRepository investorRepository;
	@Resource
	PriceSystemRepository priceSystemRepository;
	@Resource
	CompanyRepository companyRepository;
	@Resource
	TraderRepository traderRepository;
	@Resource
	SalerRepository salerRepository;
	@Resource
	DeviceBatchRepository deviceBatchRepository;
	
	@Value("${o2.qrcode.content}")
	public String DEVICE_QRCODE_CONTENT;
	
	@Value("${file.uploadFolder.excel.patch}")
	public String DEVICE_EXCEL_FILE;
	
	/**
	 * 设备信息查询实现方法
	 * 
	 * @author SongXueShuang
	 *
	 */
	@Override
	public Page<Device> findAllDevice(DeviceView deviceView, Pageable pageable) {
		Specification<Device> specification = new Specification<Device>() {
			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if(!StringUtils.isEmpty(deviceView.getMachNo())) {//设备编号
					Predicate p1 = cb.like(root.get("machNo"),"%"+deviceView.getMachNo()+"%");
					conditions.add(p1);
				}
				if(!StringUtils.isEmpty(deviceView.getSetupAddress())) {//设备安装地址	
					Predicate p3 = cb.like(root.get("setupAddress"),"%"+deviceView.getSetupAddress()+"%");
					conditions.add(p3);
				}
				if(!StringUtils.isEmpty(deviceView.getInvestorId())) {//投资商	
					Predicate p4 = cb.equal(root.get("investor").get("id"), deviceView.getInvestorId());
					conditions.add(p4);
				}
				if(!StringUtils.isEmpty(deviceView.getTraderId())) {//商家	
					Predicate p5 = cb.equal(root.get("trader").get("id"),deviceView.getTraderId());
					conditions.add(p5);
				}
				if(!StringUtils.isEmpty(deviceView.getSalerId())) {//促销员	
					Predicate p6 = cb.equal(root.get("saler").get("id"),deviceView.getSalerId());
					conditions.add(p6);
				}
				Predicate p7 = cb.equal(root.get("deviceBatch").get("id"), deviceView.getDeviceBatchId());
				conditions.add(p7);
				
				Predicate p2 = cb.equal(root.get("removed"),false);
				conditions.add(p2);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return deviceRepository.findAll(specification,pageable);
	}

	
	/**
	 * 设备信息修改查询
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Device findById(String id) {
		return deviceRepository.findOne(id);
	}
	
	@Override
	public void saveOrUpdate(DeviceView deviceView) {
		if (deviceView != null) {
			Device device = null;
			String id = deviceView.getId();
			if (StringUtils.isNotBlank(id)) {
				device = deviceRepository.findByIdAndRemoved(id, false);
				BeanUtils.copyProperties(deviceView, device);
				//更新下拉选择相关字段
				this.updateRelated(device, deviceView);
			} else {
				device = new Device();
				Date nowDate = Calendar.getInstance().getTime();
				BeanUtils.copyProperties(deviceView, device);
				this.updateRelated(device, deviceView);
				device.setCreateTime(nowDate);
				device.setLastOperateTime(nowDate);
				Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
				device.setLastOperator(principal.getUser().getUsername());
				device.setCreater(principal.getUser().getUsername());
				device.setDeviceSequence("CD" + RandomStringUtils.randomNumeric(13));
			}
			deviceRepository.saveAndFlush(device);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
	}
	
	/**
	 *设备信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		deviceRepository.deleteDevice(id);
	}
	
	/**
	 * 更新关联数据表
	 * 
	 * @param device
	 * @param deviceView
	 * @return
	 * */
	private void updateRelated(Device device,DeviceView deviceView) {
		String companyid = deviceView.getCompanyId();
		if (StringUtils.isNotBlank(companyid)) {
			Company company = companyRepository.findByIdAndRemoved(companyid, false);
			if (company != null) {
				device.setCompany(company);
			}
		}
		String investorId = deviceView.getInvestorId();
		if (StringUtils.isNotBlank(investorId)) {
			Investor investor = investorRepository.findByIdAndRemoved(investorId, false);
			if (investor != null) {
				device.setInvestor(investor);
			}
		}
		String traderId = deviceView.getTraderId();
		if (StringUtils.isNotBlank(traderId)) {
			Trader trader = traderRepository.findByIdAndRemoved(traderId, false);
			if (trader != null) {
				device.setTrader(trader);
			}
		}
		String salerId = deviceView.getSalerId();
		if (StringUtils.isNotBlank(salerId)) {
			Saler saler = salerRepository.findByIdAndRemoved(salerId, false);
			if (saler != null) {
				device.setSaler(saler);
			}
		}

		String priceSystemId = deviceView.getPriceSystemId();
		PriceSystem priceSystem = priceSystemRepository.findByIdAndRemoved(priceSystemId, false);
		if(priceSystem != null){
			device.setPriceSystem(priceSystem);
		}
		String deviceBatchId = deviceView.getDeviceBatchId();
		if(StringUtils.isEmpty(deviceBatchId)) {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		} else {
			DeviceBatch deviceBatch = deviceBatchRepository.findByIdAndRemoved(deviceBatchId, false);
			device.setDeviceBatch(deviceBatch);
		}
	}


	@Override
	public String generateQRcode(String mchId, String desPath) {

		try {
			QRCodeUtil.encode(String.format(DEVICE_QRCODE_CONTENT, mchId), mchId, desPath);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return mchId + "." + QRCodeUtil.FORMAT_NAME;
	}


	@Override
	public int batchImport(String deviceBatchId, String name, MultipartFile file) {
		File targetFile = null;
		try {
			targetFile = new File(DEVICE_EXCEL_FILE);
			if(!targetFile.isDirectory()){
				FileUtils.forceMkdir(new File(DEVICE_EXCEL_FILE));
			}
			targetFile = new File(DEVICE_EXCEL_FILE,name);
			if(!targetFile.exists() && !targetFile.isDirectory())
				file.transferTo(targetFile);
		} catch (IOException e) {
			logger.error("设备批量导入异常：{}",e.getMessage());
			throw new BusinessException(ErrorCodeEnum.ES_7001.getMessage());
		}
		
		logger.info("开始读取[{}]的文件内容.....", name);
		long readerTime = System.currentTimeMillis();
		List<DeviceExcel> deviceList = FileUtil.importExcel(DEVICE_EXCEL_FILE+File.separator+name,0,1,DeviceExcel.class);
	    if(!CollectionUtils.isEmpty(deviceList)) {
	    	 logger.info("需要导入数据一共【"+deviceList.size()+"】行");
	    	 this.devicePross(deviceList,deviceBatchId);
	    	 logger.info("读取["+name+"]的文件内容, 一共耗时："+(System.currentTimeMillis()-readerTime));
	    }
	    targetFile.delete();
		return 1;
	}
	
	private boolean devicePross(List<DeviceExcel> deviceList,String deviceBatchId) {
		List<Device> result = new ArrayList<>(deviceList.size());
		Date nowDate = Calendar.getInstance().getTime();
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		Device inDevice = null;
		DeviceExcel deviceExcel = null;
		DeviceBatch deviceBatch = deviceBatchRepository.findByIdAndRemoved(deviceBatchId, false);
		if (deviceBatch == null)
			return false;
		for (int i = 0; i < deviceList.size(); i++) {
			deviceExcel = deviceList.get(i);
			inDevice = new Device();
			if(StringUtils.isBlank(deviceExcel.getMachNo())){
				throw new BusinessException(ErrorCodeEnum.ES_9001.getMessage());
			}
			inDevice.setMachNo(deviceExcel.getMachNo());
			inDevice.setSetupAddress(deviceExcel.getSetupAddress());
			inDevice.setDeviceBatch(deviceBatch);
			inDevice.setCreateTime(nowDate);
			inDevice.setCreater(principal.getUser().getUsername());
			if (inDevice != null) {
				result.add(inDevice);
			}
		}
		if (CollectionUtils.isEmpty(result)) {
			return false;
		} else {
			deviceRepository.save(result);
			result.clear();
		}
		return true;
	}
}
