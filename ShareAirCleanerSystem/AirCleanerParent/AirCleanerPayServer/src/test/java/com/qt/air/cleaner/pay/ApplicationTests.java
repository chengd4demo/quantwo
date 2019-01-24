package com.qt.air.cleaner.pay;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ApplicationTests {
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Value("${o2.weixin.cache.name}")
	public String WEIXIN_CACHE_NAME;
	@Value("${o2.wechat.subscription.body.description}")
	public String body;
	@Test
	public void contextLoads() {
		System.out.println(body);
	}

}
