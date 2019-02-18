package com.qt.air.cleaner.market.service.report.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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
	public List<SweepCodeReportView> findAllApplyReport(Map<String, Object> params) {
		logger.info("扫码统计查询,请求参数{}", JSONUtils.valueToString(params));
		String startTime = String.valueOf(params.get("startTime"));
		String endTime = String.valueOf(params.get("endTime"));
		String type = String.valueOf(params.get("type"));
		String traderId = String.valueOf(params.get("traderId"));
		String isPage = String.valueOf(params.get("isPage"));
		String start = String.valueOf(params.get("start") == null ? 0 : params.get("start"));
		String end = String.valueOf(params.get("end") == null ? 1 : params.get("end") );
		String sql = this.buildSweepCodeReportSqlQuery(type, traderId, startTime, endTime, Boolean.parseBoolean(isPage));
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
			Map<String,String> dateMap = convertDateFormat(startTime,endTime,type);
			query.setParameter("startTime", dateMap.get("startTime"));
			query.setParameter("endTime", dateMap.get("endTime"));
		}
		if (Boolean.parseBoolean(isPage)) {
			query.setParameter("start", Integer.parseInt(start));
			query.setParameter("end", Integer.parseInt(end));
		}
		query.setResultTransformer(Transformers.aliasToBean(SweepCodeReportView.class));
		List<SweepCodeReportView> result = query.list();
		em.close();
		return result;
	}
	
	
	public Map<String,String> convertDateFormat(String startTime,String endTime,String type) {
		Map<String,String> result = new HashMap<String,String>();
		if (StringUtils.equals("month", type)) {
			startTime += "-01 00:00:00";
			result.put("startTime", startTime);
			Calendar cal = Calendar.getInstance();
			endTime += "-" +cal.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
			result.put("endTime", endTime);
		} else if(StringUtils.equals("day", type) || StringUtils.equals("week", type)){
			result.put("startTime", startTime + " 00:00:00");
			result.put("endTime", endTime + " 23:59:59");
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, Integer.parseInt(startTime));
			startTime = df.format(cal.getTime()) + " 00:00:00";
			cal.clear();
			cal.set(Calendar.YEAR, Integer.parseInt(endTime));
			cal.roll(Calendar.DAY_OF_YEAR, -1);
			endTime = df.format(cal.getTime()) + " 23:59:59";
			result.put("startTime", startTime);
			result.put("endTime", endTime);
		}
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
	private String buildSweepCodeReportSqlQuery(String type, String traderId,String startTime,String endTime, boolean isPage) {
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
			sql.append("select to_char(t.sweep_code_time, 'YYYY-MM') as dates,sum(t.total) as total, t.mach_no as machno from REP_SWEEP_CODE t ");
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
			sql.append("select to_char(t.sweep_code_time, 'YYYY') as dates,sum(t.total) as total, t.mach_no as machno from REP_SWEEP_CODE t ");
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
