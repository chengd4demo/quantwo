package com.qt.air.cleaner.scheduled;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qt.air.cleaner.scheduled.service.ReportBillingService;

@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations={"classpath:application.properties"})//加载spring配置文件
public class AppTest{

	@Autowired
	ReportBillingService reportBillingService;

	@Test
	public void jobSweepCodeReport(Date date) {
		reportBillingService.startReportBilling(new Date());
		
	}
	

}
