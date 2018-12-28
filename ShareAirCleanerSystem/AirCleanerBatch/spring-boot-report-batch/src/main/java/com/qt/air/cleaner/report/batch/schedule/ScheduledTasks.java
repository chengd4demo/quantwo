package com.qt.air.cleaner.report.batch.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.report.batch.config.BatchConfiguration;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Autowired
	private BatchConfiguration batchConfiguration;
	
	
	/**
	 * 定时任启动后,延迟10每秒后再执行定时器，
	 * 以后每一分钟执行一次
	 * @throws Exception
	 */
	@Scheduled(initialDelay=10000,fixedRate = 1000*60)
	public void fixedTimePerDayBillBatch() throws Exception{
		log.info("job begin {}", dateFormat.format(new Date()));
		batchConfiguration.run();
		log.info("job end {}", dateFormat.format(new Date()));
	}
	
}
