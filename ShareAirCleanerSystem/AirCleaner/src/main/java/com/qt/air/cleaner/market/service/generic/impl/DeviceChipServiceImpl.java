package com.qt.air.cleaner.market.service.generic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
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

import com.qt.air.cleaner.market.controller.generic.DeviceChip;
import com.qt.air.cleaner.market.service.generic.DeviceChipService;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;

@Service
@Transactional
public class DeviceChipServiceImpl implements DeviceChipService{

	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<DeviceChip> findAllDevice(DeviceChipView deviceChipView, Pageable pageable) {
		StringBuffer sql = new StringBuffer();
		long count = getDeviceChipCount(new StringBuffer(),deviceChipView);
		if(count == 0) return new PageImpl<DeviceChip>(new ArrayList<DeviceChip>(), pageable, count);
		sql = new StringBuffer();
		sql.append("SELECT machno,devicesequence,batchname,setuptime,setupaddress,legalperson,tradername,salername,800 renascencetime,employtime,case WHEN employtime > 800 then 0 ELSE 800-employtime END surplustime");
		sql.append("  FROM (select row_.*, rownum rownum_");
		sql.append("    FROM ( SELECT bill.MACH_NO machno,mk1.DEVICE_SEQUENCE devicesequence,mkb.BATCH_NAME batchname,mk1.SETUP_TIME setuptime,mk1.SETUP_ADDRESS setupaddress,mk2.LEGAL_PERSON legalperson,mk3.NAME tradername,mk4.NAME salername,sum(bill.COST_TIME/60)  employtime");
		sql.append("           FROM ACT_BILLING bill");
		sql.append("           LEFT JOIN MK_DEVICE mk1 ON bill.MACH_NO = mk1.MACH_NO");
		sql.append("           LEFT JOIN MK_DEVICE_BATCH mkb ON mk1.DEVICE_BATCH_ID = mkb.ID");
		sql.append("           LEFT JOIN MK_INVESTOR mk2 ON mk1.INVESTOR_ID = mk2.ID");
		sql.append("           LEFT JOIN MK_TRADER mk3 ON mk1.TRADER_ID = mk3.ID");
		sql.append("           LEFT JOIN MK_SALER mk4 ON mk1.SALER_ID = mk4.ID");
		sql.append("           WHERE bill.TRANSACTION_ID IS NOT NULL");
		if (StringUtils.isNotBlank(deviceChipView.getMachno())) {
			sql.append("	AND bill.MACH_NO like CONCAT('%', :machno)");
		}
		if (StringUtils.isNotBlank(deviceChipView.getDevicesequence())) {
			sql.append("	ANd mk1.DEVICE_SEQUENCE like CONCAT('%', :devicesequence)");
		}
		if (StringUtils.isNotBlank(deviceChipView.getBatchname())) {
			sql.append("	AND mkb.BATCH_NAME like CONCAT('%', :batchname)");
		}
		if (StringUtils.isNotBlank(deviceChipView.getInvestorid())) {
			sql.append("	AND mk1.INVESTOR_ID = :investorid");
		}
		if (StringUtils.isNotBlank(deviceChipView.getTraderid())) {
			sql.append("	AND mk1.TRADER_ID = :traderid");
		}
		if (StringUtils.isNotBlank(deviceChipView.getSalerid())) {
			sql.append("	AND mk1.SALER_ID = :salerid");
		}
		sql.append("	GROUP BY bill.MACH_NO,mk1.DEVICE_SEQUENCE,mkb.BATCH_NAME,mk1.SETUP_TIME,mk1.SETUP_ADDRESS,mk2.LEGAL_PERSON,mk3.NAME,mk4.NAME) row_");
		sql.append(" WHERE");
		sql.append(" ROWNUM <= :end");
		if (StringUtils.equals("warning",deviceChipView.getType())) {
			sql.append(" AND ow_.employtime >=750");
		}
		sql.append(" ) WHERE rownum_ > :start");
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("machno",StandardBasicTypes.STRING)
				.addScalar("devicesequence",StandardBasicTypes.STRING)
				.addScalar("batchname",StandardBasicTypes.STRING)
				.addScalar("setuptime",StandardBasicTypes.STRING)
				.addScalar("setupaddress",StandardBasicTypes.STRING)
				.addScalar("legalperson",StandardBasicTypes.STRING)
				.addScalar("tradername",StandardBasicTypes.STRING)
				.addScalar("salername",StandardBasicTypes.STRING)
				.addScalar("renascencetime",StandardBasicTypes.INTEGER)
				.addScalar("employtime",StandardBasicTypes.INTEGER)
				.addScalar("surplustime", StandardBasicTypes.INTEGER);
		if (StringUtils.isNotBlank(deviceChipView.getMachno())) {
			query.setParameter("machno", deviceChipView.getMachno());
		}
		if (StringUtils.isNotBlank(deviceChipView.getDevicesequence())) {
			query.setParameter("devicesequence", deviceChipView.getDevicesequence());
		}
		if (StringUtils.isNotBlank(deviceChipView.getBatchname())) {
			query.setParameter("batchname", deviceChipView.getBatchname());
		}
		if (StringUtils.isNotBlank(deviceChipView.getInvestorid())) {
			query.setParameter("investorid", deviceChipView.getInvestorid());
		}
		if (StringUtils.isNotBlank(deviceChipView.getTraderid())) {
			query.setParameter("traderid", deviceChipView.getTraderid());
		}
		if (StringUtils.isNotBlank(deviceChipView.getSalerid())) {
			query.setParameter("salerid", deviceChipView.getSalerid());
		}
		query.setParameter("start",pageable.getPageNumber());
		query.setParameter("end", pageable.getPageSize());
		query.setResultTransformer(Transformers.aliasToBean(DeviceChip.class));
		List<DeviceChip> list =  query.list();
		em.close();
		return new PageImpl<DeviceChip>(list, pageable, count);
	}

	private long getDeviceChipCount(StringBuffer sql, DeviceChipView deviceChipView) {
		sql.append("SELECT count(row_.machno) count");
		sql.append("	FROM(");
		sql.append("	SELECT bill.MACH_NO machno,mk1.DEVICE_SEQUENCE devicesequence,mkb.BATCH_NAME batchname,mk1.SETUP_TIME setuptime,mk1.SETUP_ADDRESS setupaddress,mk2.LEGAL_PERSON legalperson,mk3.NAME tradername,mk4.NAME salername");
		sql.append("		FROM ACT_BILLING bill");
		sql.append("			LEFT JOIN MK_DEVICE mk1 ON bill.MACH_NO = mk1.MACH_NO");
		sql.append("			LEFT JOIN MK_DEVICE_BATCH mkb ON mk1.DEVICE_BATCH_ID = mkb.ID");
		sql.append("			LEFT JOIN MK_INVESTOR mk2 ON mk1.INVESTOR_ID = mk2.ID");
		sql.append("			LEFT JOIN MK_TRADER mk3 ON mk1.TRADER_ID = mk3.ID");
		sql.append("			LEFT JOIN MK_SALER mk4 ON mk1.SALER_ID = mk4.ID");
		sql.append("	    WHERE bill.TRANSACTION_ID IS NOT NULL");
		if (StringUtils.isNotBlank(deviceChipView.getMachno())) {
			sql.append("	AND bill.MACH_NO like CONCAT('%', :machno)");
		}
		if (StringUtils.isNotBlank(deviceChipView.getDevicesequence())) {
			sql.append("	ANd mk1.DEVICE_SEQUENCE like CONCAT('%', :devicesequence)");
		}
		if (StringUtils.isNotBlank(deviceChipView.getBatchname())) {
			sql.append("	AND mkb.BATCH_NAME like CONCAT('%', :batchname)");
		}
		if (StringUtils.isNotBlank(deviceChipView.getInvestorid())) {
			sql.append("	AND mk1.INVESTOR_ID = :investorid");
		}
		if (StringUtils.isNotBlank(deviceChipView.getTraderid())) {
			sql.append("	AND mk1.TRADER_ID = :traderid");
		}
		if (StringUtils.isNotBlank(deviceChipView.getSalerid())) {
			sql.append("	AND mk1.SALER_ID = :salerid");
		}
		sql.append("	GROUP BY bill.MACH_NO,mk1.DEVICE_SEQUENCE,mkb.BATCH_NAME,mk1.SETUP_TIME,mk1.SETUP_ADDRESS,mk2.LEGAL_PERSON,mk3.NAME,mk4.NAME) row_");
		if(StringUtils.equals("warning",deviceChipView.getType())) {
			sql.append(" WHERE ow_.employtime >=750");
		}
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString());
		if (StringUtils.isNotBlank(deviceChipView.getMachno())) {
			query.setParameter("machno", deviceChipView.getMachno());
		}
		if (StringUtils.isNotBlank(deviceChipView.getDevicesequence())) {
			query.setParameter("devicesequence", deviceChipView.getDevicesequence());
		}
		if (StringUtils.isNotBlank(deviceChipView.getBatchname())) {
			query.setParameter("batchname", deviceChipView.getBatchname());
		}
		if (StringUtils.isNotBlank(deviceChipView.getInvestorid())) {
			query.setParameter("investorid", deviceChipView.getInvestorid());
		}
		if (StringUtils.isNotBlank(deviceChipView.getTraderid())) {
			query.setParameter("traderid", deviceChipView.getTraderid());
		}
		if (StringUtils.isNotBlank(deviceChipView.getSalerid())) {
			query.setParameter("salerid", deviceChipView.getSalerid());
		}
		long result = new BigDecimal(query.uniqueResult().toString()).longValue();;
		em.close();
		return result;
	}

}
