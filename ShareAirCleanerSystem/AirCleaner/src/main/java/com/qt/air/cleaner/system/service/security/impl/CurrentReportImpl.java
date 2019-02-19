package com.qt.air.cleaner.system.service.security.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.repository.account.AccountOutBoundRepository;
import com.qt.air.cleaner.market.repository.account.BillingRefundRepository;
import com.qt.air.cleaner.market.repository.account.BillingRepository;
import com.qt.air.cleaner.market.repository.generic.DeviceRepository;
import com.qt.air.cleaner.system.service.security.CurrentReportService;

@Service
public class CurrentReportImpl implements CurrentReportService {
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	@Resource
	AccountOutBoundRepository accountOutBoundRepository;
	@Resource
	BillingRefundRepository billingRefundRepository;
	@Resource
	BillingRepository billingRepository;
	@Resource
	DeviceRepository deviceRepository;
	@Override
	public int findCashApplyCount(Date time) {
		Map<String,Date> dateBetween = getDateBetween(time);
		Long count = accountOutBoundRepository.findCount(dateBetween.get("start"),dateBetween.get("end"));
		return count.intValue();
	}
	
	@Override
	public int findRefundApplyCount(Date time) {
		Map<String,Date> dateBetween = getDateBetween(time);
		Long count = billingRefundRepository.findCount(dateBetween.get("start"),dateBetween.get("end"));
		return count.intValue();
	}

	@Override
	public int findTodayOrder(Date time) {
		Map<String,Date> dateBetween = getDateBetween(time);
		Long count = billingRepository.findCount(dateBetween.get("start"),dateBetween.get("end"));
		return count.intValue();
	}

	@Override
	public int findVolume(Date time) {
		Map<String,Date> dateBetween = getDateBetween(time);
		Long count = billingRepository.findVolumeCount(dateBetween.get("start"),dateBetween.get("end"));
		return count.intValue();
	}

	@Override
	public int findDeviceCount(Date time) {
		Device device = new Device();
		device.setRemoved(Boolean.FALSE);
		Example<Device> example = Example.of(device);
		Long count = deviceRepository.count(example);
		return count == null ? 0 : count.intValue();
	}

	@Override
	public int findDeviceOnlineCount(Date time) {
		Long count = getDeviceOline(time);
		return count.intValue();
	}

	private Long getDeviceOline(Date time) {
		Map<String,Date> dateBetween = getDateBetween(time);
		StringBuffer sql = new StringBuffer();
		sql.append("select count(distinct t.mach_no) as onlinecount ");
		sql.append(" from act_billing t, mk_device d");
		sql.append(" where d.mach_no = t.mach_no and t.transaction_id is not null ");
		sql.append(" and (((sysdate-to_date(to_char(t.operate_time,'yyyy/mm/dd hh24:mi:ss'),'yyyy/mm/dd hh24:mi:ss'))) * 24 * 60) < t.cost_time");
		sql.append("  and (t.create_time between  :start and :end)");
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("onlinecount",StandardBasicTypes.LONG);
		query.setParameter("start", dateBetween.get("start"));
		query.setParameter("end", dateBetween.get("end"));
		
		Long result = (Long) query.uniqueResult();
		return result;
	}

	@Override
	public int findSweepCodeCount(Date time) {
		Map<String,Date> dateBetween = getDateBetween(time);
		Long count = billingRepository.findCount(dateBetween.get("start"),dateBetween.get("end"));
		return count.intValue();
	}
	
	private Map<String, Date> getDateBetween(Date date) {
		Map<String,Date> result = new HashMap<String,Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		result.put("start", calendar.getTime());
		result.put("end", date);
		return result;
	}

}
