package com.qt.air.cleaner.device;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
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
		RequestParame requestParame = new RequestParame();
		Map<String,String> pa = new HashMap<String,String>();
		pa.put("machNo", "890233029203");
		pa.put("pmValue", "4");
		requestParame.setData(pa);
		ResultInfo  resultInfo = deviceService.updatPmDataUpPlatform(requestParame);
		Gson gson = new Gson();
		String result = gson.toJson(resultInfo.getData());
		System.out.println(result);
		
		System.out.println("-----------------------------------------------ok");
	}

}
