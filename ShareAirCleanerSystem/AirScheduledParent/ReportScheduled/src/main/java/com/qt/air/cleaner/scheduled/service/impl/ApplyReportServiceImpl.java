package com.qt.air.cleaner.scheduled.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.qt.air.cleaner.scheduled.domain.ApplyReport;
import com.qt.air.cleaner.scheduled.domain.Company;
import com.qt.air.cleaner.scheduled.domain.Device;
import com.qt.air.cleaner.scheduled.domain.Investor;
import com.qt.air.cleaner.scheduled.domain.Saler;
import com.qt.air.cleaner.scheduled.domain.Trader;
import com.qt.air.cleaner.scheduled.repository.ApplyReportRepository;
import com.qt.air.cleaner.scheduled.repository.DeviceRepository;
import com.qt.air.cleaner.scheduled.service.ApplyReportService;
import com.qt.air.cleaner.scheduled.vo.ApplyReporView;

@Service
@Transactional
public class ApplyReportServiceImpl implements ApplyReportService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	ApplyReportRepository applyReportRepository;
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public void jobApplyReport(Date currentTime) {
		long count = applyReportRepository.count();
		if (count ==0) {
			logger.debug("启动定时任务首次执行设备使用统计");
			 applyHandle(null);
		} else {
			 applyHandle(currentTime);
		}
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	private void applyHandle(Date currentTime) {
		String sql = "";
		if (currentTime == null) {
			sql = "select t.mach_no as machno,to_char(t.create_time,'YYYY-MM-DD') as sweepcodetime,count(t.id) as total from act_billing t"
					+ " where t.transaction_id is not null group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc";
		} else {
			sql = "select t.mach_no as machno,to_char(t.create_time,'YYYY-MM-DD') as sweepcodetime,count(t.id) as total from act_billing t where trunc(t.create_time) = trunc(sysdate)"
					+ " and t.transaction_id is not null group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc";
		}
		try {
			EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
			Session session = em.unwrap(Session.class);
			Query query = session.createSQLQuery(sql.toString())
					.addScalar("machno",StandardBasicTypes.STRING)
					.addScalar("sweepcodetime", StandardBasicTypes.STRING)
					.addScalar("total", StandardBasicTypes.LONG);
			query.setResultTransformer(Transformers.aliasToBean(ApplyReporView.class));
			List<ApplyReporView> list =  query.list();
			em.close();
			if (!CollectionUtils.isEmpty(list)) {
				batchExecute(list,currentTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设备使用统计异常:{}",e.getMessage());
		}
		
	}
	
	/**
	 * 报表统计处理
	 * 
	 * @param currentTime
	 * @param list
	 */
	@SuppressWarnings("static-access")
	private void executeReport(Date currentTime, List<ApplyReporView> list) {
		logger.debug("执行使用统计定时任务，待统计数据共:{}条",list.size());
		Device device = null;
		Company company = null;
		Trader trader = null;
		Investor investor = null;
		Saler saler = null;
		Long total = 0L;
		String machNo = null;
		Date nowDate = currentTime == null ? Calendar.getInstance().getTime() : currentTime;
		ApplyReport applyReport = null;
		String todayDate = null;
		for(ApplyReporView applyReporView : list ) {
			machNo = applyReporView.getMachno();
			todayDate = df.format(nowDate) + '%';
			applyReport = applyReportRepository.findSweepCodeReportData(machNo,todayDate);
			total = applyReporView.getTotal();
			if (applyReport == null) {
				logger.debug("保存到使用统计表数据为:{}",new Gson().toJson(applyReporView));
				applyReport = new ApplyReport();
				device = deviceRepository.findByMachNo(applyReporView.getMachno());
				if (device != null) {
					saler = device.getSaler();
					if (saler != null) {
						applyReport.setSalerId(saler.getId());
					} 
					investor = device.getInvestor();
					if (investor != null) {
						applyReport.setInvestorId(investor.getId());
					}
					trader = device.getTrader();
					if (trader != null) {
						applyReport.setTraderId(trader.getId());
					}
					company = device.getCompany();
					if (company != null) {
						applyReport.setCompanyId(company.getId());
					}
					applyReport.setMachNo(machNo);
					applyReport.setTotal(total);
					applyReport.setCreateTime(nowDate);
					applyReport.setCreater("scheduled");
					try {
						applyReport.setCreateTime(this.convertStrToDate(applyReporView.getSweepcodetime()));
						applyReport.setSweepCodeTime(this.convertStrToDate(applyReporView.getSweepcodetime()));
					} catch (ParseException e) {
						e.printStackTrace();
						logger.error("时间转换失败");
					}
					applyReportRepository.save(applyReport);
				} else {
					logger.warn("====================================设备信息不存在,设备号:{}",applyReporView.getMachno());
				}
			} else {
				if (total != applyReport.getTotal() && !nowDate.before(applyReport.getSweepCodeTime())) {
					logger.debug("更新设备使用统计表数据为:{}",new Gson().toJson(applyReporView));
					applyReport.setLastOperator("scheduled");
					applyReport.setTotal(total);
					applyReport.setLastOperateTime(nowDate);
					applyReportRepository.saveAndFlush(applyReport);
				}
			}
		}
	}
	
	/**
	 * 分批处理数据
	 * 
	 * @param dataList
	 * @param currentTime
	 */
	private void batchExecute(List<ApplyReporView> dataList,Date currentTime) {
		 int pointsDataLimit = 50;
		 List<ApplyReporView> newList = new ArrayList<ApplyReporView>(); 
		 for(int i=0;i<dataList.size();i++){//分批次处理
			  newList.add(dataList.get(i));
			  if(pointsDataLimit == newList.size()||i == dataList.size()-1){
				  executeReport(currentTime, newList);
				  newList.clear();
			  }
		 }
	}
	
	private static Date convertStrToDate(String dateStr) throws ParseException {  
	    return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);  
	}
}
