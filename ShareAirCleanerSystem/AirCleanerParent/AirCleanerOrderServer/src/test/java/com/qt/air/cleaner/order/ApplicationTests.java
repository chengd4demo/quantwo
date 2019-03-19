package com.qt.air.cleaner.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qt.air.cleaner.order.repository.AccountInBoundRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ApplicationTests {
	@Autowired
	AccountInBoundRepository inbound;
	@Test
	public void contextLoads() {
		String accountId = inbound.findAccountId("o6q5d1WO8sVNsPz_DZY6e44tc2Mc", "IR");
		System.out.println(accountId);
	}

}
