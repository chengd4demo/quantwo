package com.qt.air.cleaner.pay;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.qt.air.cleaner.pay.domain.Token;


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
	public void contextLoads() throws InterruptedException {
		System.out.println(body);
		
		stringRedisTemplate.opsForValue().set("test", new Gson().toJson(new Token()),60,TimeUnit.SECONDS);
		Thread.sleep(1000);
		Token token = stringRedisTemplate.opsForValue().get("test") == null ? null :  new Gson().fromJson(stringRedisTemplate.opsForValue().get("test").toString(), Token.class);
		System.out.println(new Gson().toJson(token));
	}

}
