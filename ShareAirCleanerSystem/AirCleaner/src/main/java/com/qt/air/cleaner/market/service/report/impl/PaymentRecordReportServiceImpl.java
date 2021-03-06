package com.qt.air.cleaner.market.service.report.impl;

import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.market.service.report.PaymentRecordReportService;
import com.qt.air.cleaner.market.service.report.SweepCodeReportService;
import com.qt.air.cleaner.market.vo.report.PaymentRecordView;

import net.sf.json.util.JSONUtils;

@Service
public class PaymentRecordReportServiceImpl implements PaymentRecordReportService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	@Autowired
	SweepCodeReportService sweepCodeReportService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentRecordView> findAllPaymentRecordReport(Map<String, Object> params) {
		logger.info("支付统计查询,请求参数{}", JSONUtils.valueToString(params));
		String startTime = String.valueOf(params.get("startTime"));
		String endTime = String.valueOf(params.get("endTime"));
		String type = String.valueOf(params.get("type"));
		String traderId = String.valueOf(params.get("traderId"));
		String isPage = String.valueOf(params.get("isPage"));
		String start = String.valueOf(params.get("start") == null ? 0 : params.get("start"));
		String end = String.valueOf(params.get("end") == null ? 1 : params.get("end") );
		String sql = this.buildPaymentRecordReportSqlQuery(type, traderId, startTime, endTime, Boolean.parseBoolean(isPage));
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("dates",StandardBasicTypes.STRING)
				.addScalar("total", StandardBasicTypes.FLOAT)
				.addScalar("machno", StandardBasicTypes.STRING);
		if(StringUtils.isNotBlank(traderId)) {
			query.setParameter("traderId", traderId);
		}
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			Map<String,String> dateMap = sweepCodeReportService.convertDateFormat(startTime,endTime,type);
			query.setParameter("startTime", dateMap.get("startTime"));
			query.setParameter("endTime", dateMap.get("endTime"));
		}
		if (Boolean.parseBoolean(isPage)) {
			query.setParameter("start", Integer.parseInt(start));
			query.setParameter("end", Integer.parseInt(end));
		}
		query.setResultTransformer(Transformers.aliasToBean(PaymentRecordView.class));
		List<PaymentRecordView> result = query.list();
		em.close();
		return result;
	}

	private String buildPaymentRecordReportSqlQuery(String type, String traderId, String startTime, String endTime,
			boolean isPage) {
		StringBuffer sql = new StringBuffer();
		if (StringUtils.equals("day", type)) {
			sql.append("select dates, total, machno from ( ");
			sql.append("select rownum as rn, a.dates, a.total, a.machno from( ");
			sql.append("select to_char(t.sweep_code_time, 'YYYY-MM-DD') as dates,sum(t.amounts) as total,t.mach_no as machno from REP_PAYMENT_RECORD t ");
			sql.append("where t.removed = 'N' ");
			if (StringUtils.isNotBlank(traderId)) {
				sql.append("and t.trader_id = :traderId ");
			}
			if (StringUtils.isNotBlank(startTime) && StringUtils.isNotEmpty(endTime)) {
				sql.append("and (t.sweep_code_time between to_date(:startTime, 'yyyy/mm/dd hh24:mi:ss')  and  to_date(:endTime, 'yyyy/mm/dd hh24:mi:ss')) ");
			}
			sql.append("group by to_char(t.sweep_code_time, 'YYYY-MM-DD'),t.mach_no order by to_char(t.sweep_code_time, 'YYYY-MM-DD'),t.mach_no )a)");
			if (isPage) {
				sql.append("where rn >=:start and rn <= :end");
			}
		} else if (StringUtils.equals("week", type)) {
			sql.append("select dates, total, machno from ( ");
			sql.append("select rownum as rn, a.dates, a.total, a.machno from( ");
			sql.append("select to_char(next_day(t.sweep_code_time + 15 / 24 - 7, 2), 'YYYY-MM-DD') as dates,sum(t.amounts) as total,t.mach_no as machno from REP_PAYMENT_RECORD t ");
			sql.append("where t.removed = 'N' ");
			if (StringUtils.isNotBlank(traderId)) {
				sql.append("and t.trader_id = :traderId ");
			}
			if (StringUtils.isNotBlank(startTime) && StringUtils.isNotEmpty(endTime)) {
				sql.append("and (t.sweep_code_time between to_date(:startTime, 'yyyy/mm/dd hh24:mi:ss')  and  to_date(:endTime, 'yyyy/mm/dd hh24:mi:ss')) ");
			}
			sql.append("group by to_char(next_day(t.sweep_code_time + 15 / 24 - 7, 2), 'YYYY-MM-DD'),t.mach_no order by to_char(next_day(t.sweep_code_time + 15 / 24 - 7, 2), 'YYYY-MM-DD'),t.mach_no )a)");
			if (isPage) {
				sql.append("where rn >=:start and rn <= :end");
			}
		} else if (StringUtils.equals("month", type)) {
			sql.append("select dates, total, machno from ( ");
			sql.append("select rownum as rn, a.dates, a.total, a.machno from( ");
			sql.append("select to_char(t.sweep_code_time, 'YYYY-MM') as dates,sum(t.amounts) as total, t.mach_no as machno from REP_PAYMENT_RECORD t ");
			sql.append("where t.removed = 'N' ");
			if (StringUtils.isNotBlank(traderId)) {
				sql.append("and t.trader_id = :traderId ");
			}
			sql.append("and (t.sweep_code_time between to_date(:startTime, 'yyyy/mm/dd hh24:mi:ss')  and  to_date(:endTime, 'yyyy/mm/dd hh24:mi:ss')) ");
			sql.append("group by to_char(t.sweep_code_time, 'YYYY-MM'),t.mach_no order by to_char(t.sweep_code_time, 'YYYY-MM'),t.mach_no)a)");
			if (isPage) {
				sql.append("where rn >=:start and rn <= :end");
			}
		} else if (StringUtils.equals("year", type)) {
			sql.append("select dates, total, machno from ( ");
			sql.append("select rownum as rn, a.dates, a.total, a.machno from( ");
			sql.append("select to_char(t.sweep_code_time, 'YYYY') as dates,sum(t.amounts) as total, t.mach_no as machno from REP_PAYMENT_RECORD t ");
			sql.append("where t.removed = 'N' ");
			if (StringUtils.isNotBlank(traderId)) {
				sql.append("and t.trader_id = :traderId ");
			}
			sql.append("and (t.sweep_code_time between to_date(:startTime, 'yyyy/mm/dd hh24:mi:ss')  and  to_date(:endTime, 'yyyy/mm/dd hh24:mi:ss')) ");
			sql.append("group by to_char(t.sweep_code_time, 'YYYY'),t.mach_no order by to_char(t.sweep_code_time, 'YYYY'),t.mach_no)a)");
			if (isPage) {
				sql.append("where rn >=:start and rn <= :end");
			}
		}
		return sql.toString();
		
	}

}
