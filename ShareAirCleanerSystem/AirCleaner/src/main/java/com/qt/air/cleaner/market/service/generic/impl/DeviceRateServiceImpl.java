package com.qt.air.cleaner.market.service.generic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.market.service.generic.DeviceRateService;
import com.qt.air.cleaner.market.vo.generic.DeviceRateView;

@Service
@Transactional
public class DeviceRateServiceImpl implements DeviceRateService{
	
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<DeviceRateView> findAllDeviceRate(DeviceRateView deviceRateView, Pageable pageable) {
		String machNo = deviceRateView.getMachno();
		String deviceSequence = deviceRateView.getDevicesequence();
		StringBuffer sql = new StringBuffer();
		long count = getDeviceRateCount(new StringBuffer(), machNo, deviceSequence);
		if(count == 0) return new PageImpl<DeviceRateView>(new ArrayList<DeviceRateView>(), pageable, count);
		sql = new StringBuffer();
		sql.append("SELECT MACH_NO machno,DEVICE_SEQUENCE devicesequence,BATCH_NAME batchname,SETUP_TIME setuptime,SETUP_ADDRESS setupaddress,investor_legal_person investorlegalperson,trader_name tradername,saler_name salername,to_char(billing_create_time,'yyyy-MM-dd HH24:mi:ss') lastusetime");
		sql.append("  FROM (select row_.*, rownum rownum_");
		sql.append("	FROM (SELECT d.*, t2.CREATE_TIME AS billing_create_time,t3.LEGAL_PERSON investor_legal_person,t4.NAME trader_name,t5.BATCH_NAME,t6.NAME saler_name");
		sql.append("	FROM mk_device d");
		sql.append("		LEFT JOIN ( SELECT b2.MACH_NO, b2.CREATE_TIME, ROW_NUMBER() OVER (PARTITION BY b2.mach_no ORDER BY b2.create_time desc) AS rn FROM ACT_BILLING b2) t2");
		sql.append("			ON t2.MACH_NO = d.MACH_NO AND t2.rn = 1");
		sql.append("		LEFT JOIN (SELECT MK1.ID,MK1.LEGAL_PERSON FROM MK_INVESTOR MK1) t3");
		sql.append("			ON t3.ID = d.INVESTOR_ID");
		sql.append("		LEFT JOIN (SELECT MK2.ID,MK2.NAME FROM MK_TRADER MK2) t4");
		sql.append("			ON t4.ID = d.TRADER_ID");
		sql.append("		LEFT JOIN (SELECT MK3.ID,MK3.BATCH_NAME from MK_DEVICE_BATCH MK3) t5");
		sql.append("			ON t5.ID = d.DEVICE_BATCH_ID");
		sql.append("		LEFT JOIN (SELECT MK4.ID,MK4.NAME FROM MK_SALER MK4) t6");
		sql.append("			ON t6.ID = d.SALER_ID");
		sql.append("	WHERE NOT EXISTS (");
		sql.append("		SELECT t1.mach_no, t1.cou");
		sql.append("			FROM (");
		sql.append("				SELECT b.MACH_NO, COUNT(b.id) AS cou FROM act_billing b");
		sql.append("					WHERE b.CREATE_TIME >= trunc(SYSDATE - 7) GROUP BY b.MACH_NO");
		sql.append("			) t1");
		sql.append("			WHERE t1.cou >= 2");
		sql.append("			AND t1.MACH_NO = d.MACH_NO)");
		if (StringUtils.isNotBlank(machNo)) {
			sql.append("		AND d.MACH_NO like concat('%',:machNo)" );
		}
		if (StringUtils.isNotEmpty(deviceSequence)) {
			sql.append(" 		AND d.DEVICE_SEQUENCE like concat('%',:deviceSequence)" );
		}
		sql.append("	) row_");
		sql.append("	where rownum <= :end )");
		sql.append("where rownum_ > :start");	
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("machno",StandardBasicTypes.STRING)
				.addScalar("devicesequence",StandardBasicTypes.STRING)
				.addScalar("batchname",StandardBasicTypes.STRING)
				.addScalar("setuptime",StandardBasicTypes.STRING)
				.addScalar("setupaddress",StandardBasicTypes.STRING)
				.addScalar("investorlegalperson",StandardBasicTypes.STRING)
				.addScalar("tradername",StandardBasicTypes.STRING)
				.addScalar("salername",StandardBasicTypes.STRING)
				.addScalar("lastusetime",StandardBasicTypes.STRING);
		if (StringUtils.isNotBlank(machNo)) {
			query.setParameter("machNo", machNo);
		}
		if (StringUtils.isNotEmpty(deviceSequence)) {
			query.setParameter("deviceSequence", deviceSequence);
		}
		query.setParameter("start",pageable.getPageNumber());
		query.setParameter("end", pageable.getPageSize());
		query.setResultTransformer(Transformers.aliasToBean(DeviceRateView.class));
		List<DeviceRateView> list =  query.list();
		em.close();
		return new PageImpl<DeviceRateView>(list, pageable, count);
	}

	private long getDeviceRateCount(StringBuffer sql,String machNo,String deviceSequence) {
		sql.append("SELECT count(d.id) count");
		sql.append("	FROM mk_device d");
		sql.append("	WHERE NOT EXISTS (");
		sql.append("		SELECT t1.mach_no, t1.cou");
		sql.append("			FROM (");
		sql.append("				SELECT b.MACH_NO, COUNT(b.id) AS cou FROM act_billing b");
		sql.append("					WHERE b.CREATE_TIME >= trunc(SYSDATE - 7) GROUP BY b.MACH_NO");
		sql.append("			) t1");
		sql.append("			WHERE t1.cou >= 2");
		sql.append("			AND t1.MACH_NO = d.MACH_NO)");
		if (StringUtils.isNotBlank(machNo)) {
			sql.append("		AND d.MACH_NO like concat('%',:machNo)" );
		}
		if (StringUtils.isNotEmpty(deviceSequence)) {
			sql.append(" 		AND d.DEVICE_SEQUENCE like concat('%',:deviceSequence)" );
		}
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString());
		if (StringUtils.isNotBlank(machNo)) {
			query.setParameter("machNo", machNo);
		}
		if (StringUtils.isNotEmpty(deviceSequence)) {
			query.setParameter("deviceSequence", deviceSequence);
		}
		long result = 0L;
		result = new BigDecimal(query.uniqueResult().toString()).longValue();
		em.close();
		return result;
		
	}
	
	

	
}
