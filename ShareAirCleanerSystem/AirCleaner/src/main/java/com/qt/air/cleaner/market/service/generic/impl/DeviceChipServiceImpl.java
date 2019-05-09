package com.qt.air.cleaner.market.service.generic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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

import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.market.domain.generic.DeviceChip;
import com.qt.air.cleaner.market.domain.generic.DeviceChipIn;
import com.qt.air.cleaner.market.repository.generic.DeviceChipRepository;
import com.qt.air.cleaner.market.service.generic.DeviceChipService;
import com.qt.air.cleaner.market.vo.generic.DeviceChipView;

@Service
@Transactional
public class DeviceChipServiceImpl implements DeviceChipService{

	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	@Autowired
	DeviceChipRepository deviceChipRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<DeviceChip> findAllDevice(DeviceChipView deviceChipView, Pageable pageable) {
		StringBuffer sql = new StringBuffer();
		long count = getDeviceChipCount(new StringBuffer(),deviceChipView);
		if(count == 0) return new PageImpl<DeviceChip>(new ArrayList<DeviceChip>(), pageable, count);
		sql = new StringBuffer();
		sql.append("SELECT machno, devicesequence, batchname, setuptime, setupaddress");
				sql.append("	, legalperson, tradername, salername, employtime, renascencetime,case WHEN employtime > 800 then 0 ELSE 800-employtime END surplustime");
				sql.append(" FROM (");
				sql.append("	SELECT row_.*, rownum AS rownum_");
				sql.append("	FROM (");
				sql.append("		SELECT r.machno, r.devicesequence, r.batchname, r.setuptime, r.setupaddress");
				sql.append("			, r.legalperson, r.tradername, r.salername, r.employtime, r.renascencetime");
				sql.append("		FROM (");
				sql.append("			SELECT machno, devicesequence, batchname, setuptime, setupaddress");
				sql.append("				, legalperson, tradername, salername, employtime, 800 AS renascencetime");
				sql.append("				, row_number() OVER (PARTITION BY machno ORDER BY employtime) AS devicechip");
				sql.append("			FROM (");
				sql.append("				SELECT bill.MACH_NO AS machno, mk1.DEVICE_SEQUENCE AS devicesequence, mkb.BATCH_NAME AS batchname, mk1.SETUP_TIME AS setuptime, mk1.SETUP_ADDRESS AS setupaddress");
				sql.append("					, mk2.LEGAL_PERSON AS legalperson, mk3.NAME AS tradername, mk4.NAME AS salername");
				sql.append("					, SUM(bill.COST_TIME / 60) AS employtime");
				sql.append("				FROM ACT_BILLING bill");
				sql.append("					LEFT JOIN MK_DEVICE mk1 ON bill.MACH_NO = mk1.MACH_NO");
				sql.append("					LEFT JOIN MK_DEVICE_BATCH mkb ON mk1.DEVICE_BATCH_ID = mkb.ID");
				sql.append("					LEFT JOIN MK_INVESTOR mk2 ON mk1.INVESTOR_ID = mk2.ID");
				sql.append("					LEFT JOIN MK_TRADER mk3 ON mk1.TRADER_ID = mk3.ID");
				sql.append("					LEFT JOIN MK_SALER mk4 ON mk1.SALER_ID = mk4.ID");
				sql.append("				WHERE EXISTS (");
				sql.append("						SELECT chip.mach_no");
				sql.append("						FROM mk_device_chip chip");
				sql.append("					)");
				sql.append("					AND bill.TRANSACTION_ID IS NOT NULL");
				if (StringUtils.isNotBlank(deviceChipView.getMachno())) {
					sql.append("	AND bill.MACH_NO like CONCAT('%', :machno)");
				}
				if (StringUtils.isNotBlank(deviceChipView.getDevicesequence())) {
					sql.append("	AND mk1.DEVICE_SEQUENCE like CONCAT('%', :devicesequence)");
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
				sql.append("				GROUP BY bill.MACH_NO, mk1.DEVICE_SEQUENCE, mkb.BATCH_NAME, mk1.SETUP_TIME, mk1.SETUP_ADDRESS, mk2.LEGAL_PERSON, mk3.NAME, mk4.NAME");
				sql.append("				UNION");
				sql.append("				SELECT bill.MACH_NO AS machno, mk1.DEVICE_SEQUENCE AS devicesequence, mkb.BATCH_NAME AS batchname, mk1.SETUP_TIME AS setuptime, mk1.SETUP_ADDRESS AS setupaddress");
				sql.append("					, mk2.LEGAL_PERSON AS legalperson, mk3.NAME AS tradername, mk4.NAME AS salername");
				sql.append("					, SUM(bill.COST_TIME / 60) AS employtime");
				sql.append("				FROM ACT_BILLING bill");
				sql.append("					LEFT JOIN (");
				sql.append("						SELECT chip.mach_no, chip.change_time");
				sql.append("						FROM mk_device_chip chip");
				sql.append("					) c");
				sql.append("					ON c.mach_no = c.mach_no");
				sql.append("					LEFT JOIN MK_DEVICE mk1 ON bill.MACH_NO = mk1.MACH_NO");
				sql.append("					LEFT JOIN MK_DEVICE_BATCH mkb ON mk1.DEVICE_BATCH_ID = mkb.ID");
				sql.append("					LEFT JOIN MK_INVESTOR mk2 ON mk1.INVESTOR_ID = mk2.ID");
				sql.append("					LEFT JOIN MK_TRADER mk3 ON mk1.TRADER_ID = mk3.ID");
				sql.append("					LEFT JOIN MK_SALER mk4 ON mk1.SALER_ID = mk4.ID");
				sql.append("				WHERE bill.TRANSACTION_ID IS NOT NULL");
				sql.append("					AND bill.create_time BETWEEN c.change_time AND sysdate");
				sql.append("					AND bill.mach_no = c.mach_no");
				if (StringUtils.isNotBlank(deviceChipView.getMachno())) {
					sql.append("	AND bill.MACH_NO like CONCAT('%', :machno)");
				}
				if (StringUtils.isNotBlank(deviceChipView.getDevicesequence())) {
					sql.append("	AND mk1.DEVICE_SEQUENCE like CONCAT('%', :devicesequence)");
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
				sql.append("				GROUP BY bill.MACH_NO, mk1.DEVICE_SEQUENCE, mkb.BATCH_NAME, mk1.SETUP_TIME, mk1.SETUP_ADDRESS, mk2.LEGAL_PERSON, mk3.NAME, mk4.NAME");
				sql.append("			)");
				sql.append("		) r");
				sql.append("		WHERE r.devicechip = 1");
				sql.append("	) row_");
				sql.append("	WHERE ROWNUM <= :end");
				if (StringUtils.equals("warning",deviceChipView.getType())) {
					sql.append(" AND row_.employtime >=750");
				}
				sql.append(")");
				sql.append(" WHERE rownum_ > :start");
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
		sql.append("SELECT COUNT(machno) AS count");
				sql.append(" FROM (");
				sql.append("	SELECT row_.*");
				sql.append("	FROM (");
				sql.append("		SELECT r.machno, r.devicesequence, r.batchname, r.setuptime, r.setupaddress");
				sql.append("			, r.legalperson, r.tradername, r.salername, r.employtime, r.renascencetime");
				sql.append("		FROM (");
				sql.append("			SELECT machno, devicesequence, batchname, setuptime, setupaddress");
				sql.append("				, legalperson, tradername, salername, employtime, 800 AS renascencetime");
				sql.append("				, row_number() OVER (PARTITION BY machno ORDER BY employtime) AS devicechip");
				sql.append("			FROM (");
				sql.append("				SELECT bill.MACH_NO AS machno, mk1.DEVICE_SEQUENCE AS devicesequence, mkb.BATCH_NAME AS batchname, mk1.SETUP_TIME AS setuptime, mk1.SETUP_ADDRESS AS setupaddress");
				sql.append("					, mk2.LEGAL_PERSON AS legalperson, mk3.NAME AS tradername, mk4.NAME AS salername");
				sql.append("					, SUM(bill.COST_TIME / 60) AS employtime");
				sql.append("				FROM ACT_BILLING bill");
				sql.append("					LEFT JOIN MK_DEVICE mk1 ON bill.MACH_NO = mk1.MACH_NO");
				sql.append("					LEFT JOIN MK_DEVICE_BATCH mkb ON mk1.DEVICE_BATCH_ID = mkb.ID");
				sql.append("					LEFT JOIN MK_INVESTOR mk2 ON mk1.INVESTOR_ID = mk2.ID");
				sql.append("					LEFT JOIN MK_TRADER mk3 ON mk1.TRADER_ID = mk3.ID");
				sql.append("					LEFT JOIN MK_SALER mk4 ON mk1.SALER_ID = mk4.ID");
				sql.append("				WHERE EXISTS (");
				sql.append("						SELECT chip.mach_no");
				sql.append("						FROM mk_device_chip chip");
				sql.append("					)");
				sql.append("					AND bill.TRANSACTION_ID IS NOT NULL");
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
				sql.append("				GROUP BY bill.MACH_NO, mk1.DEVICE_SEQUENCE, mkb.BATCH_NAME, mk1.SETUP_TIME, mk1.SETUP_ADDRESS, mk2.LEGAL_PERSON, mk3.NAME, mk4.NAME");
				sql.append("				UNION");
				sql.append("				SELECT bill.MACH_NO AS machno, mk1.DEVICE_SEQUENCE AS devicesequence, mkb.BATCH_NAME AS batchname, mk1.SETUP_TIME AS setuptime, mk1.SETUP_ADDRESS AS setupaddress");
				sql.append("					, mk2.LEGAL_PERSON AS legalperson, mk3.NAME AS tradername, mk4.NAME AS salername");
				sql.append("					, SUM(bill.COST_TIME / 60) AS employtime");
				sql.append("				FROM ACT_BILLING bill");
				sql.append("					LEFT JOIN (");
				sql.append("						SELECT chip.mach_no, chip.change_time");
				sql.append("						FROM mk_device_chip chip");
				sql.append("					) c");
				sql.append("					ON c.mach_no = c.mach_no");
				sql.append("					LEFT JOIN MK_DEVICE mk1 ON bill.MACH_NO = mk1.MACH_NO");
				sql.append("					LEFT JOIN MK_DEVICE_BATCH mkb ON mk1.DEVICE_BATCH_ID = mkb.ID");
				sql.append("					LEFT JOIN MK_INVESTOR mk2 ON mk1.INVESTOR_ID = mk2.ID");
				sql.append("					LEFT JOIN MK_TRADER mk3 ON mk1.TRADER_ID = mk3.ID");
				sql.append("					LEFT JOIN MK_SALER mk4 ON mk1.SALER_ID = mk4.ID");
				sql.append("				WHERE bill.TRANSACTION_ID IS NOT NULL");
				sql.append("					AND bill.create_time BETWEEN c.change_time AND sysdate");
				sql.append("					AND bill.mach_no = c.mach_no");
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
				sql.append("				GROUP BY bill.MACH_NO, mk1.DEVICE_SEQUENCE, mkb.BATCH_NAME, mk1.SETUP_TIME, mk1.SETUP_ADDRESS, mk2.LEGAL_PERSON, mk3.NAME, mk4.NAME");
				sql.append("			)");
				sql.append("		) r");
				sql.append("		WHERE r.devicechip = 1");
				sql.append("	) row_");
				if (StringUtils.equals("warning", deviceChipView.getType())) {
					sql.append(" WHERE row_.employtime >=750)");
				} else {
					sql.append(")");
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

	@Override
	public void updateDeviceChip(String machNo) {
		DeviceChipIn chipIn =  deviceChipRepository.findByMachNo(machNo);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		// 分
		calendar.set(Calendar.MINUTE, 0);
		// 秒
		calendar.set(Calendar.SECOND, 0);
		// 毫秒
		calendar.set(Calendar.MILLISECOND, 0);
		Date nowDate = calendar.getTime();
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		if(chipIn == null) {
			chipIn = new DeviceChipIn();
			chipIn.setMachNo(machNo);
			chipIn.setCreater(principal.getUser().getUsername());
			chipIn.setCreateTime(nowDate);
		} else {
			chipIn.setLastOperator(principal.getUser().getUsername());
			chipIn.setLastOperateTime(nowDate);
		}
		chipIn.setChangeTime(nowDate);
		deviceChipRepository.saveAndFlush(chipIn);
		
	}

}
