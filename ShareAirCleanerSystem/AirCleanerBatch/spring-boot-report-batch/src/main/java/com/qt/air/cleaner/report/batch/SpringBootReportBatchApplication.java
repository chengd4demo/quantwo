package com.qt.air.cleaner.report.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootReportBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReportBatchApplication.class, args);
//		/*System.exit(SpringApplication
//				.exit(SpringApplication.run(SpringBootReportBatchApplication.class, args)));*/
	}
}
