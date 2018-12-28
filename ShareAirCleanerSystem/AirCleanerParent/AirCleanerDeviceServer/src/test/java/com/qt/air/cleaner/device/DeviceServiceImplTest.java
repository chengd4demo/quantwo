package com.qt.air.cleaner.device;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.device.Application;
import com.qt.air.cleaner.device.domain.Device;
import com.qt.air.cleaner.device.repository.DeviceRepository;
import com.qt.air.cleaner.device.service.DeviceService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DeviceServiceImplTest {

	@Autowired DeviceService deviceService;
	@Autowired DeviceRepository deviceRepository;
	

	@org.junit.Test
	public void test() {
		ResultInfo  resultInfo = deviceService.queryDeviceMonitor("869300033631205");
		Gson gson = new Gson();
		String result = gson.toJson(resultInfo.getData());
		System.out.println(result);
		
		System.out.println("-----------------------------------------------ok");
	}

}
