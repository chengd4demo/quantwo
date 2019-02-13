package com.qt.air.cleaner.market.controller.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.market.domain.generic.Device;
import com.qt.air.cleaner.market.service.generic.DeviceService;
import com.qt.air.cleaner.market.service.generic.TraderService;
import com.qt.air.cleaner.market.service.report.SweepCodeReportService;
import com.qt.air.cleaner.market.vo.report.SweepCodeReportRespView;
import com.qt.air.cleaner.market.vo.report.SweepCodeReportView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("market/report")
public class ReportController {
	private final String SWEEP_CODE_REPORT_INDEX = "view/market/report/querySweepCodeIndex",
			PAYMENT_RECORD_REPORT_INDEX = "view/market/report/queryPaymentRecordIndex",
			APPLY_REPORT_INDEX =  "view/market/report/queryApplyIndex";
			
	@Autowired
	TraderService traderService;
	@Autowired
	DeviceService deviceService;
	@Autowired
	SweepCodeReportService sweepCodeReportService;
	
	/**
	 * 扫码统计页面入口
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/sweeptcode/index")
	public String sweeptCodeIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return SWEEP_CODE_REPORT_INDEX;
	}
	
	@RequestMapping("/device/query")
	@ResponseBody
	public SweepCodeReportRespView querySweepCodeReport(@RequestBody Map<String,String> params) throws ParseException{
		String traderId = params.get("traderId");
		SweepCodeReportRespView result = new SweepCodeReportRespView();
		List<Device> deviceList = deviceService.findByTraderId(traderId);
		//商家下面没有设备不进行报表展示
		if (CollectionUtils.isEmpty(deviceList)) {
			return result;
		} 
		String[] devices = new String[]{};
		for(int i=0;i<deviceList.size();i++) {
			devices[i] = deviceList.get(i).getMachNo();
		}
		result.setDevices(devices);
		//X轴日期刻度处理
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String type = params.get("type");
		if(StringUtils.equals("day", params.get("type")) && StringUtils.isNotBlank(startTime) 
				&& StringUtils.isNotBlank(endTime)) {
			String format = null;
			if (StringUtils.equals("day", type) || StringUtils.equals("week", type)) {
				format = "yyyy-MM-dd";
			} else if (StringUtils.equals("month", type)) {
				format = "yyyy-MM";
			} else if (StringUtils.equals("year", type)) {
				format = "yyyy";
			}
			SimpleDateFormat df = new SimpleDateFormat(format);
			List<String> dateList = this.getDatesBetweenTwoDate(df.parse(startTime), df.parse(endTime),df,type);
			result.setDates(dateList.toArray(new String[dateList.size()]));
		}
		//Y轴
		List<SweepCodeReportView>  sweepCodeList = sweepCodeReportService.findAllApplyReport(params);
		JSONArray jsonArray = new JSONArray();
		if (!CollectionUtils.isEmpty(sweepCodeList)) {
			JSONObject jsonObject = new JSONObject();
			Long[] totals = null;
			for (String device : devices) {
				jsonObject.put("name", device);
				jsonObject.put("type", "line");
				jsonObject.put("stack", "总量");
				totals = getSweepCodeTotal(device,sweepCodeList);
				jsonObject.put("data", totals);
				jsonArray.add(jsonObject);
			}
			result.setSeries(jsonArray);
		}
		return result;
	}
	
	
	
	private Long[] getSweepCodeTotal(String device,List<SweepCodeReportView> sweepCodeList) {
		List<Long> totals = new ArrayList<Long>();
		SweepCodeReportView sweepCode = null;
		for (int i=0; i<sweepCodeList.size();i++) {
			sweepCode = sweepCodeList.get(i);
			if(StringUtils.equals(device, sweepCode.getMachno())) {
				totals.add(sweepCode.getTotal());
			}
		}
		return totals.toArray(new Long[totals.size()]);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/paymentrecord/index")
	public String paymentRecordIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return PAYMENT_RECORD_REPORT_INDEX;
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/apply/index")
	public String applyIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return APPLY_REPORT_INDEX;
	}
	
	private List<String> getDatesBetweenTwoDate(Date beginDate, Date endDate,SimpleDateFormat df,String type){
		 List<String> lDate = new ArrayList<String>();  
	        lDate.add(df.format(beginDate));// 把开始时间加入集合  
	        Calendar cal = Calendar.getInstance();  
	        // 使用给定的 Date 设置此 Calendar 的时间  
	        cal.setTime(beginDate);  
	        boolean bContinue = true;  
	        
	        while (bContinue) {  
	            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
	        	if (StringUtils.equals("day", type)){
	        		cal.add(Calendar.DAY_OF_MONTH, 1);  
	        	} else if(StringUtils.equals("week", type)){
	        		cal.add(Calendar.WEDNESDAY, 1);
	        	} else if (StringUtils.equals("month", type)) {
	        		cal.add(Calendar.MONTH, 1); 
	        	} else if(StringUtils.equals("year", type)) {
	        		cal.add(Calendar.YEAR, 1);
	        	}
	            // 测试此日期是否在指定日期之后  
	            if (endDate.after(cal.getTime())) {  
	                lDate.add(df.format(cal.getTime()));  
	            } else {  
	                break;  
	            }  
	        }  
	        lDate.add(df.format(endDate));// 把结束时间加入集合  
	        return lDate;  
	} 
//	public static void main(String[] args) throws ParseException {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy");
//		List  list = getDatesBetweenTwoDate(df.parse("2015"),df.parse("2019"),df,"year");
//		System.out.println(list.toString());
//	}
}
