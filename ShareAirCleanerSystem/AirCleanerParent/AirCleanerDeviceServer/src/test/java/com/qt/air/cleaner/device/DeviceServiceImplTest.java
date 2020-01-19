package com.qt.air.cleaner.device;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.device.domain.Device;
import com.qt.air.cleaner.device.domain.Investor;
import com.qt.air.cleaner.device.domain.Trader;
import com.qt.air.cleaner.device.repository.DeviceRepository;
import com.qt.air.cleaner.device.repository.InvestorRepository;
import com.qt.air.cleaner.device.service.DeviceService;
import com.qt.air.cleaner.device.utils.DeviceUtil;
import com.qt.air.cleaner.device.vo.DeviceResult;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DeviceServiceImplTest {

	@Autowired DeviceService deviceService;
	@Autowired DeviceRepository deviceRepository;
	@Autowired InvestorRepository investorRepository;

	@org.junit.Test
	public void test() {
//		RequestParame requestParame = new RequestParame();
//		Map<String,String> pa = new HashMap<String,String>();
//		pa.put("machNo", "890233029203");
//		pa.put("pmValue", "4");
//		requestParame.setData(pa);
//		ResultInfo  resultInfo = deviceService.updatPmDataUpPlatform(requestParame);
//		Gson gson = new Gson();
//		String result = gson.toJson(resultInfo.getData());
//		System.out.println(result);
//		
//		System.out.println("-----------------------------------------------ok");
		List<Investor> investorList = investorRepository.findAll();
		List<Investor> netInvestorLit = new ArrayList<Investor>();
		for(Investor investor : investorList) {
			if (investor.getLegalPerson().equals("杨力")) {
				//investorList.remove(i);
			} else {
				System.out.println(investor.getLegalPerson());
				netInvestorLit.add(investor);
			}
		}
		Investor currenInvestor = null;
		List<Device>  deviceList = null;
		for(int i=0;i<netInvestorLit.size();i++) {
			currenInvestor = netInvestorLit.get(i);
			deviceList = deviceRepository.findByInvestor_Id(currenInvestor.getId());
			if(!CollectionUtils.isEmpty(deviceList)) {
				try {
					getDeviceNoOnLineInfo(deviceList,currenInvestor);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		//List<Device> device = deviceRepository.findAll();
	}

	private void getDeviceNoOnLineInfo(List<Device> deviceList,Investor currenInvestor) throws InterruptedException {
		String machNo = null;
		DeviceResult deviceResult = null;
		Trader trader = null;
		for(Device device:deviceList) {
			machNo = device.getMachNo();
			deviceResult = DeviceUtil.queryDeviceState(machNo);
			if (deviceResult.getResult() == 0 ) {
				if(deviceResult.getOnline() == 0 ) {
					//trader = device.getTrader();
					//if(trader != null) {
						System.out.println("投资商姓名：" + currenInvestor.getLegalPerson() + " | " + "电话：" + currenInvestor.getPhoneNumber() +  " | " +"设备号：" + device.getMachNo());
					//}
					//System.out.println("投资商姓名：" + currenInvestor.getLegalPerson() + " | " + "电话：" + currenInvestor.getPhoneNumber() + " | " + "商户名：" + trader.getName() + " | " + "联系电话："+ trader.getPhoneNumber() +  " | "+ "安装地址：" + trader.getAddress() +  " | " +"设备号：" + device.getMachNo());
					Thread.sleep(1000);
				}
			}
		}
		
	}

}
