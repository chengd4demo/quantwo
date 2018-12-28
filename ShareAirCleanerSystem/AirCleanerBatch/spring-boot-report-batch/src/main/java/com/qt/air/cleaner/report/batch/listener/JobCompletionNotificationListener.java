package com.qt.air.cleaner.report.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.report.batch.model.Billing;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("!!! JOB FINISHED! Time to verify the results");
			List<Billing> results = jdbcTemplate.query("SELECT mach_no FROM act_billing", (rs, row) -> {
                Billing billings = new Billing();
                billings.setMachNo(rs.getString("mach_no"));
                return billings;
            });
			for(Billing billing : results) {
				logger.info("Found <" + billing.toString() + "> in the database.");
			}
		}
	}
		
}
