package com.qt.air.cleaner.scheduled.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.qt.air.cleaner.scheduled.domain.PaymentRecordReport;
import com.qt.air.cleaner.scheduled.domain.Saler;
import com.qt.air.cleaner.scheduled.domain.Trader;
import com.qt.air.cleaner.scheduled.repository.DeviceRepository;
import com.qt.air.cleaner.scheduled.repository.PaymentRecordReportRepository;
import com.qt.air.cleaner.scheduled.service.PaymentRecordReportService;
import com.qt.air.cleaner.scheduled.vo.PaymentRecordReportView;

@Service
@Transactional
public class PaymentRecordReportServiceImpl implements PaymentRecordReportService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	PaymentRecordReportRepository paymentRecordReportRepository;
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	DeviceRepository deviceRepository;
	@Override
	public void jobPaymentRecordReport(Date currentTime) {
		long count = paymentRecordReportRepository.count();
		if (count ==0) {
			logger.debug("启动定时任务首次执行支付金额统计");
			paymentRecordHandle(null);
		} else {
			paymentRecordHandle(currentTime);
		}
		
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	private void paymentRecordHandle(Date currentTime) {
		String sql = "";
		if (currentTime == null) {
			sql = "select t.mach_no as machno,to_char(t.create_time,'YYYY-MM-DD') as sweepcodetime,sum(t.amount) as amount from act_billing t"
					+ " where t.transaction_id is not null"
					+ " group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc";
		} else {
			sql = "select t.mach_no as machno,to_char(t.create_time,'YYYY-MM-DD') as sweepcodetime,sum(t.amount) as amount from act_billing t where trunc(t.create_time) = trunc(sysdate)"
					+ " and t.transaction_id is not null"
					+ " group by t.mach_no, to_char(t.create_time,'YYYY-MM-DD') order by  to_char(t.create_time,'YYYY-MM-DD')  desc";
		}
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("machno",StandardBasicTypes.STRING)
				.addScalar("sweepcodetime", StandardBasicTypes.STRING)
				.addScalar("amount", StandardBasicTypes.FLOAT);
		query.setResultTransformer(Transformers.aliasToBean(PaymentRecordReportView.class));
		List<PaymentRecordReportView> list =  query.list();
		em.close();
		if (!CollectionUtils.isEmpty(list)) {
			logger.debug("执行支付金额统计定时任务，待统计数据共:{}条",list.size());
			Device device = null;
			Company company = null;
			Trader trader = null;
			Investor investor = null;
			Saler saler = null;
			Float amount = 0.00f;
			String machNo = null;
			Date nowDate = currentTime == null ? Calendar.getInstance().getTime() : currentTime;
			PaymentRecordReport paymentRecordReport = null;
			String todayDate = null;
			for(PaymentRecordReportView paymentRecordReportView : list ) {
				machNo = paymentRecordReportView.getMachno();
				todayDate = df.format(currentTime) + "%";
				paymentRecordReport = paymentRecordReportRepository.findPaymentRecordReportData(machNo,todayDate);
				amount = paymentRecordReportView.getAmount();
				if (paymentRecordReport == null) {
					logger.debug("保存到支付金额统表数据为:{}",new Gson().toJson(paymentRecordReportView));
					paymentRecordReport = new PaymentRecordReport();
					device = deviceRepository.findByMachNo(paymentRecordReportView.getMachno());
					if (device != null) {
						saler = device.getSaler();
						if (saler != null) {
							paymentRecordReport.setSalerId(saler.getId());
						} 
						investor = device.getInvestor();
						if (investor != null) {
							paymentRecordReport.setInvestorId(investor.getId());
						}
						trader = device.getTrader();
						if (trader != null) {
							paymentRecordReport.setTraderId(trader.getId());
						}
						company = device.getCompany();
						if (company != null) {
							paymentRecordReport.setCompanyId(company.getId());
						}
						paymentRecordReport.setMachNo(machNo);
						paymentRecordReport.setAmounts(amount);
						paymentRecordReport.setCreateTime(nowDate);
						paymentRecordReport.setCreater("scheduled");
						try {
							paymentRecordReport.setCreateTime(this.convertStrToDate(paymentRecordReportView.getSweepcodetime()));
							paymentRecordReport.setSweepCodeTime(this.convertStrToDate(paymentRecordReportView.getSweepcodetime()));
						} catch (ParseException e) {
							e.printStackTrace();
							logger.error("时间转换失败");
						}
						paymentRecordReportRepository.save(paymentRecordReport);
					} else {
						logger.info("====================================设备信息不存在,设备号:{}",paymentRecordReportView.getMachno());
					}
				} else {
					if (amount != paymentRecordReport.getAmounts()  && !nowDate.before(paymentRecordReport.getSweepCodeTime())) {
						logger.info("更新支付金额统计表数据为:{}",new Gson().toJson(paymentRecordReportView));
						paymentRecordReport.setLastOperator("scheduled");
						paymentRecordReport.setAmounts(amount);
						paymentRecordReport.setLastOperateTime(nowDate);
						paymentRecordReportRepository.saveAndFlush(paymentRecordReport);
					}
				}
			}
		}
	}
	
	private static Date convertStrToDate(String dateStr) throws ParseException {  
	    return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);  
	}

}
