package com.qt.air.cleaner.device.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.device.domain.Device;
import com.qt.air.cleaner.device.domain.PriceModel;
import com.qt.air.cleaner.device.domain.PriceSystem;
import com.qt.air.cleaner.device.domain.PriceValue;
import com.qt.air.cleaner.device.repository.DeviceRepository;
import com.qt.air.cleaner.device.repository.PriceModelRepository;
import com.qt.air.cleaner.device.repository.PriceSystemRepository;
import com.qt.air.cleaner.device.service.DeviceService;
import com.qt.air.cleaner.device.utils.DeviceUtil;
import com.qt.air.cleaner.device.vo.DeviceMonitor;
import com.qt.air.cleaner.device.vo.DeviceResult;
import com.qt.air.cleaner.device.vo.DeviceStatus;
import com.qt.air.cleaner.device.vo.Price;
import com.qt.air.cleaner.device.vo.TraderDevice;


@RestController
public class DeviceServiceImpl implements DeviceService {
	private static Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
	
	/**
	 * 注入持久层
	 */
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private PriceModelRepository priceModelRepository;
	@Autowired
	private PriceSystemRepository priceSystemRepository;	
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	/**
	 * 
	 * 设备使用价格列表查询
	 * 
	 * @param machNo
	 * @return
	 */
	@Override
	public ResultInfo queryDeviceStatus(@PathVariable("deviceId") String machNo) {
		logger.info("execute method queryDeviceStatus() param --> machNo:{}", machNo);
		DeviceStatus status = new DeviceStatus();
		try {
			DeviceUtil.uploadDevicePm25(machNo);
			DeviceResult deviceResult = DeviceUtil.queryDeviceState(machNo);
			status.setDeviceId(machNo);
			status.setOnLine(deviceResult.getOnline());
			status.setTurnOn(deviceResult.getTurnOn());
			status.setPm25(deviceResult.getPm25());
		} catch (Exception ex) {
			logger.error("物联网平台连接错误：", ex);
			status.setAvailable(false);
			status.setMessage("空气净化器不可用！");
		}
		if (status.getOnLine() == DeviceResult.ON_LINE_ENABLE && status.getTurnOn() == DeviceResult.TURN_ON_FALSE) {
			status.getPrice().addAll(this.queryPriceValueByMachNo(machNo));
			status.setMessage("空气净化器可用！");
			status.setAvailable(true);
		} else if (status.getOnLine() == DeviceResult.ON_LINE_DISABLE) {
			status.setAvailable(false);
			status.setMessage("空气净化器离线！");
		} else if (status.getTurnOn() == DeviceResult.ON_LINE_ENABLE) {
			status.getPrice().addAll(this.queryPriceValueByMachNo(machNo));
			status.setAvailable(false);
			status.setMessage("空气净化器已启用！");
		} else {
			status.setAvailable(false);
			status.setMessage("空气净化器未知状态！");
		} 
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", status);
	}

	/**
	 * 通过价格体系id查询价格值
	 * 
	 * @param priceSystemId
	 * @return
	 */
	private List<PriceValue> queryPriceValuesBySystemId(String priceSystemId) {
		PriceSystem priceSystem = priceSystemRepository.findByIdAndRemoved(priceSystemId, false);
		return this.queryPriceValuesByModelId(priceSystem.getPriceModeId());
		
	}
	
	/**
	 * 根据价格模型id获取对应价格列表
	 * 
	 * @param priceModelId
	 * @return
	 */
	public List<PriceValue> queryPriceValuesByModelId(String priceModelId) {
		PriceModel priceModel = priceModelRepository.findByIdAndRemoved(priceModelId, false);
		Set<PriceValue> priceValueSet = priceModel.getValues();
		List<PriceValue> priceValueList = new ArrayList<PriceValue>(priceValueSet);
		Collections.sort(priceValueList, new Comparator<PriceValue>() {
			@Override
			public int compare(PriceValue value1, PriceValue value2) {

				return value1.getCostTime() > value2.getCostTime() ? 1 : -1;
			}
		});
		return priceValueList;
	}

	/**
	 * 设备编号价格查询
	 * 
	 * @param machNo
	 * @return
	 */
	public List<Price> queryPriceValueByMachNo(String machNo){
		List<Price> result = new ArrayList<>();
		Device device = deviceRepository.findByMachNo(machNo);
		List<PriceValue> values = this.queryPriceValuesBySystemId(device.getPriceSystem().getId());
		Price price = null;
		for (PriceValue value : values) {
			price = new Price();
			price.setPriceId(value.getId());
			price.setUnitPrice(value.getValue());
			price.setCostTime(value.getCostTime());
			price.setDiscount(value.getDiscount() * 100);
			price.setRealPrice(value.getRealValue());
			result.add(price);
		}
		return result;
		
	}

	/**
	 * 设备监控列表查询
	 * 
	 * @param parame
	 * @param pageable
	 * @return
	 */
	@Override
	public ResultInfo queryDeviceMonitorPage(@RequestBody RequestParame requestParame) {
		List<DeviceMonitor> devices = this.findDeviceMonitorPage(requestParame);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", devices);
	}

	/**
	 * 查询设备监控列表
	 * 
	 * @param param
	 * @param requestParame
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DeviceMonitor> findDeviceMonitorPage(RequestParame requestParame) {
			String traderId = requestParame.getData().get("traderId");
			String investorId = requestParame.getData().get("investorId");
			String customerId = requestParame.getData().get("customerId");
			StringBuffer sql = new StringBuffer();
			sql.append("select device_id as deviceid,state as devicestate,tradername,address,mach_no as machno,to_char(create_time,'yyyy-mm-dd hh24:mi:ss') as usedate,costtime,lasttime,unitprice,devicesequence");
			sql.append("  from (select row_.*, rownum rownum_");
			sql.append("	          from (select b.create_time,");
			sql.append("                      b.device_id,");
			sql.append("                     b.mach_no,");
			sql.append("                      t.name as tradername,t.address,");
			sql.append("                     b.unit_price as unitPrice,b.cost_time as costtime,d.device_sequence as devicesequence,");
			sql.append("                      row_number() OVER(PARTITION BY b.mach_no ORDER BY b.create_time desc ) as row_flg,");
			sql.append("                     case");
			sql.append("                       when ceil(((sysdate -");
			sql.append("                                  to_date(to_char(b.operate_time,");
			sql.append("                                                   'yyyy/mm/dd hh24:mi:ss'),");
			sql.append("                                            'yyyy/mm/dd hh24:mi:ss'))) * 24 * 60) >");
			sql.append("                             b.cost_time then");
			sql.append("                         '使用结束'");
			sql.append("                        else");
			sql.append("                         '正在使用'");
			sql.append("                      end state,");
			sql.append("                      case");
			sql.append("                        when b.cost_time -");
			sql.append("                             ceil(((sysdate -");
			sql.append("                                 to_date(to_char(b.operate_time,");
			sql.append("                                                    'yyyy/mm/dd hh24:mi:ss'),");
			sql.append("                                           'yyyy/mm/dd hh24:mi:ss'))) * 24 * 60) > 0 then");
			sql.append("                         round((b.cost_time -");
			sql.append("                               ceil(((sysdate -");
			sql.append("                                     to_date(to_char(b.operate_time,");
			sql.append("                                                       'yyyy/mm/dd hh24:mi:ss'),");
			sql.append("                                               'yyyy/mm/dd hh24:mi:ss'))) * 24 * 60)) / 60,");
			sql.append("                               2)");
			sql.append("                        else");
			sql.append("                         0");
			sql.append("                      end lasttime");
			sql.append("                 from act_billing b, mk_device d, mk_trader t, mk_investor i");
			sql.append("                where b.device_id = d.id");
			sql.append("                  and d.trader_id = t.id");
			sql.append("                  and d.investor_id = i.id");
			if(StringUtils.isNotBlank(traderId)) {
				sql.append("                  and (t.id = :traderId)");
			} else if(StringUtils.isNotBlank(investorId)){
				sql.append("                  and (i.id = :investorId)");
			} else if(StringUtils.isNotBlank(customerId)){
				sql.append("                  and (b.creater = :customerId)");
			}
			sql.append("                order by b.create_time desc) row_");
			sql.append("        where rownum <= :end and row_.row_flg = '1')");
			sql.append("where rownum_ > :start");
			//消费者
			
			EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sql.toString())
					.addScalar("deviceid",StandardBasicTypes.STRING)
					.addScalar("devicestate",StandardBasicTypes.STRING)
					.addScalar("tradername",StandardBasicTypes.STRING)
					.addScalar("machno",StandardBasicTypes.STRING)
					.addScalar("usedate",StandardBasicTypes.STRING)
					.addScalar("costtime",StandardBasicTypes.INTEGER)
					.addScalar("lasttime",StandardBasicTypes.INTEGER)
					.addScalar("address",StandardBasicTypes.STRING)
					.addScalar("unitprice",StandardBasicTypes.FLOAT)
					.addScalar("devicesequence",StandardBasicTypes.STRING);
			if (StringUtils.isNotBlank(traderId)) {
				query.setParameter("traderId", traderId);
			} else if (StringUtils.isNotBlank(investorId)) {
				query.setParameter("investorId", investorId);
			} else if (StringUtils.isNotBlank(customerId)) {
				query.setParameter("customerId", customerId);
			}
			Integer start = requestParame.getPage().getStart();
			Integer end = requestParame.getPage().getEnd();
			query.setParameter("start", start);
			query.setParameter("end", end);
			query.setResultTransformer(Transformers.aliasToBean(DeviceMonitor.class));
			List<DeviceMonitor> list =  query.list();
			em.close();
		return list;
	}

	/**
	 * 设备监控信息
	 * 
	 * @param machNo
	 * @return
	 */
	@Override
	public ResultInfo queryDeviceMonitor(@PathVariable("deviceId") String machNo) {
		logger.info("execute method queryDeviceMonitor() param --> machNo:{}", machNo);
		DeviceMonitor monitor =this.findDeviceMonitor(machNo);		
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", monitor);
	}
	
	/**
	 * 
	 *  查询设备监控信息详情
	 *  
	 *  @param machNo
	 *  @return
	 * 
	 */
	public DeviceMonitor findDeviceMonitor(String machNo){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT device_id AS deviceid, mach_no AS machno, state AS devicestate, costtime, lasttime");
		sql.append(" FROM (");
		sql.append(" SELECT b.create_time, b.device_id, b.mach_no, b.cost_time AS costtime");
		sql.append(", CASE ");
		sql.append(" WHEN CEIL((SYSDATE - TO_DATE(TO_CHAR(b.operate_time, 'yyyy/mm/dd hh24:mi:ss'), 'yyyy/mm/dd"); sql.append("hh24:mi:ss')) * 24 * 60) > b.cost_time THEN '使用结束'");
		sql.append(" ELSE '正在使用'");
		sql.append(" END AS state");
		sql.append(", CASE");
		sql.append(" WHEN b.cost_time - CEIL((SYSDATE - TO_DATE(TO_CHAR(b.operate_time, 'yyyy/mm/dd hh24:mi:ss'),"); sql.append("'yyyy/mm/dd hh24:mi:ss')) * 24 * 60) > 0 THEN ROUND((b.cost_time - CEIL((SYSDATE -"); sql.append("TO_DATE(TO_CHAR(b.operate_time, 'yyyy/mm/dd hh24:mi:ss'), 'yyyy/mm/dd hh24:mi:ss')) * 24 * 60)) / 60, 2)");
		sql.append(" ELSE 0");
		sql.append(" END AS lasttime");
		sql.append(" FROM act_billing b");
		sql.append(" WHERE b.mach_no = :machNo");
		sql.append(" ORDER BY b.create_time DESC");
		sql.append(")");
		sql.append(" WHERE ROWNUM = 1");
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("deviceid",StandardBasicTypes.STRING)
				.addScalar("devicestate",StandardBasicTypes.STRING)
				.addScalar("machno",StandardBasicTypes.STRING)
				.addScalar("costtime",StandardBasicTypes.INTEGER)
				.addScalar("lasttime",StandardBasicTypes.INTEGER);
		query.setParameter("machNo", machNo);
		query.setResultTransformer(Transformers.aliasToBean(DeviceMonitor.class));
		DeviceMonitor deviceMonitor = (DeviceMonitor) query.uniqueResult();
		em.close();
		return deviceMonitor;		
	}

	/**
	 * 
	 *  设备控制(关)
	 *  
	 *  @param machNo
	 *  @return
	 * 
	 */
	@Override
	public ResultInfo deviceTurnOff(@PathVariable("deviceId") String machNo) {
		logger.info("execute method DeviceTurnoff() param --> machNo:{}", machNo);
		DeviceStatus status = new DeviceStatus();
		Integer result = 1; 
		try {
			DeviceResult deviceResult = DeviceUtil.queryDeviceState(machNo);
			if(deviceResult.getOnline() == DeviceResult.ON_LINE_ENABLE) {
				result = DeviceUtil.turnOffDevice(machNo);
			}
		} catch (Exception e) {
			logger.error("物联网平台连接错误：", e);
			status.setAvailable(false);
			status.setMessage("空气净化器不可用！");
		}		
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", result);
	}

	/**
	 * 
	 *  设备控制(开)
	 *  
	 *  @param machNo
	 *  @return
	 * 
	 */
	
	@Override
	public ResultInfo deviceTurnOn(@RequestBody Map<String,Object> parame) {
		Integer result = 1;
		try {
			if(null != parame.get("machNo") && null != parame.get("sec")) {
				String machNo = parame.get("machNo").toString();
				Integer sec = Integer.parseInt( parame.get("sec").toString());
				logger.info("execute method DeviceTurnNo() param --> machNo:{}", machNo);
				DeviceResult deviceResult = DeviceUtil.queryDeviceState(machNo);
				if(deviceResult.getOnline() == DeviceResult.ON_LINE_ENABLE) {
					result = DeviceUtil.turnOnDevice(machNo, sec);
					return new ResultInfo (String.valueOf(ResultCode.SC_OK), "success", result);
				}
			}
		} catch (Exception e) {
			logger.error("物联网平台连接错误：", e);
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", result);
	}

	/**
	 * 查询投资商下商家
	 * 
	 * @param param
	 * @param requestParame
	 * @return
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	private List<TraderDevice>findInvestorForTrader(RequestParame requestParame){
		String investorId = requestParame.getData().get("investorId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count, name, usecount, address,traderid ");
		sql.append("FROM (");												
		sql.append("	SELECT row_.*, rownum AS rownum_");
		sql.append("	FROM (");
		sql.append("		SELECT d1.count, t.name, t.address,t.id AS traderid");
		sql.append("			, nvl(d3.usecount, 0) AS usecount");
		sql.append("		FROM mk_trader t");
		sql.append("		INNER JOIN (");
		sql.append("			SELECT COUNT(d.mach_no) AS count, d.trader_id, d.investor_id");
		sql.append("			FROM mk_device d");
		sql.append("			GROUP BY d.trader_id, d.investor_id");
		sql.append("		) d1 ON d1.trader_id = t.id ");
		sql.append("			LEFT JOIN (");
		sql.append("				SELECT COUNT(b.id) AS usecount, d2.trader_id");
		sql.append("				FROM act_billing b, mk_device d2");
		sql.append("				WHERE b.mach_no = d2.mach_no");
		sql.append("					AND b.cost_time - ceil((SYSDATE - to_date(to_char(b.operate_time, 'yyyy/mm/dd"); 
		sql.append("							hh24:mi:ss'), 'yyyy/mm/dd hh24:mi:ss')) * 24 * 60) > 0");
		sql.append("					AND d2.investor_id =:investorid");
		sql.append("				GROUP BY d2.trader_id");
		sql.append("			) d3 ON d1.trader_id = d3.trader_id"); 
		sql.append("		WHERE d1.investor_id =:investorid");
		sql.append("	) row_");
		sql.append("	WHERE rownum <= :end");
		sql.append(")");
		sql.append("WHERE rownum_ > :start");
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("traderid",StandardBasicTypes.STRING)
				.addScalar("address",StandardBasicTypes.STRING)
				.addScalar("name",StandardBasicTypes.STRING)
				.addScalar("count",StandardBasicTypes.INTEGER)
				.addScalar("usecount",StandardBasicTypes.INTEGER);
		query.setParameter("investorid", investorId);
		Integer start = requestParame.getPage().getStart();
		Integer end = requestParame.getPage().getEnd();
		query.setParameter("start", start);
		query.setParameter("end", end);
		query.setResultTransformer(Transformers.aliasToBean(TraderDevice.class));
		List<TraderDevice> list =  query.list();
		em.close();
	return list;
		
	}
	
	/**
	 * 投资商下商家列表查询
	 * 
	 * @param parame
	 * @param pageable
	 * @return 
	 * 
	 */
	@Override
	public ResultInfo queryInvestorForTrader(@RequestBody RequestParame requestParame) {
		logger.info("execute method queryInvestorForTrader() param --> requestParame:{}", requestParame);
		List<TraderDevice> traderDevice = this.findInvestorForTrader(requestParame);
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", traderDevice);
	}
	
	/** 设备详细
	 * @param parame
	 * @return
	 */
	@Override
	public String queryDevice(String deviceSequence) {
		String machNo = null;
		Device device = deviceRepository.findByDeviceSequence(deviceSequence);
		if (device != null) machNo = device.getMachNo();
		return machNo;
	}

	
	
	
	
	
	
	

}
