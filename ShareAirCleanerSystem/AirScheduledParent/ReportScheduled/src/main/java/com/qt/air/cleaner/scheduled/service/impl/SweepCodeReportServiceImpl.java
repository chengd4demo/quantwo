package com.qt.air.cleaner.scheduled.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import com.qt.air.cleaner.scheduled.domain.Company;
import com.qt.air.cleaner.scheduled.domain.Device;
import com.qt.air.cleaner.scheduled.domain.Investor;
import com.qt.air.cleaner.scheduled.domain.Saler;
import com.qt.air.cleaner.scheduled.domain.SweepCodeReport;
import com.qt.air.cleaner.scheduled.domain.Trader;
import com.qt.air.cleaner.scheduled.repository.DeviceRepository;
import com.qt.air.cleaner.scheduled.repository.SweepCodeReportRepository;
import com.qt.air.cleaner.scheduled.service.SweepCodeReportService;
import com.qt.air.cleaner.scheduled.vo.SweepCodeReportView;

@Service
@Transactional
public class SweepCodeReportServiceImpl implements SweepCodeReportService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	DeviceRepository deviceRepository;
	@Resource
	SweepCodeReportRepository sweepCodeReportRepository;
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@PostConstruct
	public void init() {
		sweepCodeReportRepository.deleteAll();
		firstSweepCodeReport();
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private void firstSweepCodeReport() {
		logger.debug("启动定时任务时执行扫码率首次统计");
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		String sql = "select t.mach_no as machno,to_char(t.create_time,'YYYY-MM-DD') as sweepcodetime,count(t.id) as total from act_billing t group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc";
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("machno",StandardBasicTypes.STRING)
				.addScalar("sweepcodetime", StandardBasicTypes.STRING)
				.addScalar("total", StandardBasicTypes.LONG);
		query.setResultTransformer(Transformers.aliasToBean(SweepCodeReportView.class));
		List<SweepCodeReportView> list =  query.list();
		em.close();
		SweepCodeReport sweepCodeReport = null;
		if (!CollectionUtils.isEmpty(list)) {
			Device device = null;
			Company company = null;
			Trader trader = null;
			Investor investor = null;
			Saler saler = null;
			logger.debug("扫码率统计条数为：{}",list.size());
			for(SweepCodeReportView sweepCodeReportView : list ) {
				sweepCodeReport = new SweepCodeReport();
				device = deviceRepository.findByMachNo(sweepCodeReportView.getMachno());
				if (device != null) {
					saler = device.getSaler();
					if (saler != null) {
						sweepCodeReport.setSalerId(saler.getId());
					}
					investor = device.getInvestor();
					if (investor != null) {
						sweepCodeReport.setInvestorId(investor.getId());
					}
					trader = device.getTrader();
					if (trader != null) {
						sweepCodeReport.setTraderId(trader.getId());
					}
					company = device.getCompany();
					if (company != null) {
						sweepCodeReport.setCompanyId(company.getId());
					}
					sweepCodeReport.setMachNo(sweepCodeReportView.getMachno());
					sweepCodeReport.setTotal(sweepCodeReportView.getTotal());
					sweepCodeReport.setCreater("scheduled");
					try {
						sweepCodeReport.setCreateTime(this.convertStrToDate(sweepCodeReportView.getSweepcodetime()));
						sweepCodeReport.setSweepCodeTime(this.convertStrToDate(sweepCodeReportView.getSweepcodetime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					sweepCodeReportRepository.save(sweepCodeReport);
				} else {
					logger.warn("====================================设备信息不存在,设备号：" + sweepCodeReportView.getMachno());
				}
			}
			
		}
	}
	
	private static Date convertStrToDate(String dateStr) throws ParseException {  
	    return new SimpleDateFormat("yyyy-MM-DD").parse(dateStr);  
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void jobSweepCodeReport(Date currentTime) {
		logger.debug("扫码率统计定时任务执行，间隔时间5分钟");
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		String sql = "select t.mach_no as machno,to_char(t.create_time,'YYYY-MM-DD') as sweepcodetime,count(t.id) as total from act_billing t where trunc(t.create_time) = trunc(sysdate) group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc";
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("machno",StandardBasicTypes.STRING)
				.addScalar("sweepcodetime", StandardBasicTypes.STRING)
				.addScalar("total", StandardBasicTypes.LONG);
		query.setResultTransformer(Transformers.aliasToBean(SweepCodeReportView.class));
		List<SweepCodeReportView> list =  query.list();
		em.close();
		SweepCodeReport sweepCodeReport = null;
		if (!CollectionUtils.isEmpty(list)) {
			logger.debug("扫码率统计定时任务执行，待统计数据共:{}条",list.size());
			String machNo = null;
			Device device = null;
			Company company = null;
			Trader trader = null;
			Investor investor = null;
			Saler saler = null;
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(currentTime);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Long total = 0L;
			Date nowDate = Calendar.getInstance().getTime();
			for(SweepCodeReportView sweepCodeReportView : list ) {
				machNo = sweepCodeReportView.getMachno();
				sweepCodeReport = sweepCodeReportRepository.findByMachNoAndSweepCodeTime(machNo, calendar.getTime());
				total = sweepCodeReportView.getTotal();
				if (sweepCodeReport == null) {
					logger.debug("保存到扫码率统计表数据为:{}",new Gson().toJson(sweepCodeReportView));
					sweepCodeReport = new SweepCodeReport();
					device = deviceRepository.findByMachNo(sweepCodeReportView.getMachno());
					if (device != null) {
						saler = device.getSaler();
						if (saler != null) {
							sweepCodeReport.setSalerId(saler.getId());
						}
						investor = device.getInvestor();
						if (investor != null) {
							sweepCodeReport.setInvestorId(investor.getId());
						}
						trader = device.getTrader();
						if (trader != null) {
							sweepCodeReport.setTraderId(trader.getId());
						}
						company = device.getCompany();
						if (company != null) {
							sweepCodeReport.setCompanyId(company.getId());
						}
						sweepCodeReport.setMachNo(machNo);
						sweepCodeReport.setTotal(total);
						sweepCodeReport.setCreateTime(nowDate);
						sweepCodeReport.setCreater("scheduled");
						try {
							sweepCodeReport.setSweepCodeTime(this.convertStrToDate(sweepCodeReportView.getSweepcodetime()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						sweepCodeReportRepository.save(sweepCodeReport);
					} else {
						System.out.println("====================================设备信息不存在,设备号：" + sweepCodeReportView.getMachno());
					}
				} else {
					logger.debug("更新扫码率统计表数据为:{}",new Gson().toJson(sweepCodeReportView));
					sweepCodeReport.setLastOperator("scheduled");
					sweepCodeReport.setLastOperateTime(nowDate);
					sweepCodeReport.setTotal(total);
					sweepCodeReportRepository.saveAndFlush(sweepCodeReport);
				}
			}
		}
	}  

}
