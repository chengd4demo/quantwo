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

import com.qt.air.cleaner.market.service.report.SweepCodeReportService;
import com.qt.air.cleaner.market.vo.report.SweepCodeReportView;

import net.sf.json.util.JSONUtils;

@Service
public class SweepCodeReportServiceImpl implements SweepCodeReportService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	/**
	 * 扫码统计分页查询
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SweepCodeReportView> findAllApplyReport(Map<String, String> params) {
		logger.info("扫码统计查询,请求参数{}", JSONUtils.valueToString(params));
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String type = params.get("type");
		String traderId = params.get("traderId");
		String isPage = params.get("isPage");
		String start = params.get("start");
		String end = params.get("end");
		String sql = this.buildApplyReportSqlQuery(type, traderId, startTime, endTime, Boolean.parseBoolean(isPage));
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Session session = em.unwrap(Session.class);
		Query query = session.createSQLQuery(sql.toString())
				.addScalar("dates",StandardBasicTypes.STRING)
				.addScalar("total", StandardBasicTypes.LONG)
				.addScalar("machno", StandardBasicTypes.STRING);
		if(StringUtils.isNotBlank(traderId)) {
			query.setParameter("traderId", traderId);
		}
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			query.setParameter("startTime", startTime);
			query.setParameter("endTime", endTime);
		}
		if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
			query.setParameter("start", Integer.parseInt(start));
			query.setParameter("end", Integer.parseInt(end));
		}
		query.setResultTransformer(Transformers.aliasToBean(SweepCodeReportView.class));
		List<SweepCodeReportView> result = query.list();
		return result;
	}
	
	
	/**
	 * 根据日期分类构建SQL查询语句
	 * 
	 * @param type
	 * @param traderId
	 * @param startTime
	 * @param endTime
	 * @param isPage
	 * @return
	 */
	private String buildApplyReportSqlQuery(String type, String traderId,String startTime,String endTime, boolean isPage) {
		StringBuffer sql = new StringBuffer();
		if (StringUtils.equals("day", type)) {
			sql.append("select dates, total, machno from ( ");
			sql.append("select rownum as rn, a.dates, a.total, a.machno from( ");
			sql.append("select to_char(t.sweep_code_time, 'YYYY-MM-DD') as dates,sum(t.total) as total,t.mach_no as machno from REP_SWEEP_CODE t ");
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
			sql.append("select to_char(next_day(t.sweep_code_time + 15 / 24 - 7, 2), 'YYYY-MM-DD') as dates,sum(t.total) as total,t.mach_no as machno from REP_SWEEP_CODE t ");
			sql.append("where t.removed = 'N' ");
			if (StringUtils.isNotBlank(traderId)) {
				sql.append("and t.trader_id = :traderId ");
			}
			sql.append("and (t.sweep_code_time between to_date(:startTime, 'yyyy/mm/dd hh24:mi:ss')  and  to_date(:endTime, 'yyyy/mm/dd hh24:mi:ss')) ");
			sql.append("group by to_char(next_day(t.sweep_code_time + 15 / 24 - 7, 2),t.mach_no order by to_char(next_day(t.sweep_code_time + 15 / 24 - 7, 2),t.mach_no )a)");
			if (isPage) {
				sql.append("where rn >=:start and rn <= :end");
			}
		} else if (StringUtils.equals("month", type)) {
			sql.append("select dates, total, machno from ( ");
			sql.append("select rownum as rn, a.dates, a.total, a.machno from( ");
			sql.append("select to_char(t.sweep_code_time, 'YYYY-MM') as dates as dates,sum(t.total) as total, t.mach_no as machno from REP_SWEEP_CODE t ");
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
			sql.append("select to_char(t.sweep_code_time, 'YYYY') as dates as dates,sum(t.total) as total, t.mach_no as machno from REP_SWEEP_CODE t ");
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
