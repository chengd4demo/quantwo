package com.qt.air.cleaner.market.controller.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.qt.air.cleaner.market.service.report.ApplyReportService;
import com.qt.air.cleaner.market.service.report.PaymentRecordReportService;
import com.qt.air.cleaner.market.service.report.SweepCodeReportService;
import com.qt.air.cleaner.market.vo.report.ApplayRespView;
import com.qt.air.cleaner.market.vo.report.ApplyReportView;
import com.qt.air.cleaner.market.vo.report.PaymentRecordRespView;
import com.qt.air.cleaner.market.vo.report.PaymentRecordView;
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
	@Autowired
	PaymentRecordReportService paymentRecordReportService;
	@Autowired
	ApplyReportService applyReportService;
	
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
	
	@RequestMapping(method = RequestMethod.GET, path = "/apply/index")
	public String applyIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return APPLY_REPORT_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/paymentrecord/index")
	public String paymentRecordIndex(Model model) {
		model.addAttribute("traders", traderService.findAll(false));
		return PAYMENT_RECORD_REPORT_INDEX;
	}
	
	@RequestMapping("/applay/query")
	@ResponseBody
	public ApplayRespView queryApplay(@RequestBody Map<String,Object> params) throws ParseException {
		String traderId = params.get("traderId").toString();
		ApplayRespView result = new ApplayRespView();
		List<Device> deviceList = deviceService.findByTraderId(traderId);
		//商家下面没有设备不进行报表展示
		if (CollectionUtils.isEmpty(deviceList)) {
			return result;
		} 
		String[] devices = new String[deviceList.size()];
		for(int i=0;i<deviceList.size();i++) {
			devices[i] = deviceList.get(i).getMachNo();
		}
		//时间范围处理
		String startTime = String.valueOf(params.get("startTime"));
		String endTime = String.valueOf(params.get("endTime"));
		String type = params.get("type").toString();
		SimpleDateFormat df = null;
		String format = null;
		List<String> dateList = null;
		if(StringUtils.isNotBlank(startTime) 
				&& StringUtils.isNotBlank(endTime)) {
			if (StringUtils.equals("day", type) || StringUtils.equals("week", type)) {
				format = "yyyy-MM-dd";
			} else if (StringUtils.equals("month", type)) {
				format = "yyyy-MM";
			} else if (StringUtils.equals("year", type)) {
				format = "yyyy";
			}
			df = new SimpleDateFormat(format);
			dateList = this.getDatesBetweenTwoDate(df.parse(startTime), df.parse(endTime),df,type);
		}
		List<ApplyReportView> applayList = applyReportService.findAllApplyReport(params);
		if (!CollectionUtils.isEmpty(applayList)) {
			//柱状图显示处理逻辑
			result.setRespBar(getBarView(dateList,applayList));
			//拼图数据处理
			result.setRespPie(getPieDatasView(dateList,applayList));
		} else {
			return new ApplayRespView();
		}
		return result;
	}
	
	/**
	 * 设备使用统计饼图数据处理
	 * @param dateList 
	 * @param applayList
	 * @return
	 */
	private JSONObject getPieDatasView(List<String> dateList, List<ApplyReportView> applayList) {
		JSONObject result = new JSONObject();
		if (!CollectionUtils.isEmpty(dateList) && !CollectionUtils.isEmpty(applayList)) {
			result.put("legend_date", dateList);
			String dateStr = null;
			JSONObject contentObj = new JSONObject();
			JSONArray contentArray = new JSONArray();
			for(int i=0;i<dateList.size();i++) {
				dateStr = dateList.get(i);
				contentObj.put("value", getPieContents(dateStr,applayList));
				contentObj.put("name", dateStr);
				contentArray.add(contentObj);
			}
			result.put("series_date", contentArray);
		}
		return result;
	}

	private Long getPieContents(String dateStr, List<ApplyReportView> applayList) {
		Long total = 0L;
		for(ApplyReportView applyReport : applayList) {
			if(applyReport != null) {
				if (StringUtils.equals(dateStr, applyReport.getDates())) {
					total = applyReport.getTotal();
					break;
				}
			}
		}
		return total;
	}

	/**
	 * 设备使用统计柱形图数据处理
	 * 
	 * @param dateList
	 * @param applayList
	 * @return
	 */
	private JSONObject getBarView(List<String> dateList, List<ApplyReportView> applayList) {
		JSONObject result = new JSONObject();
		if (!CollectionUtils.isEmpty(dateList) && !CollectionUtils.isEmpty(applayList)) {
			result.put("date", dateList);
			String dateStr = null;
			Long[] series = new Long[dateList.size()];
			for(int i=0;i<dateList.size();i++) {
				dateStr = dateList.get(i);
				series[i] = getContents(dateStr,applayList);
			}
			result.put("series_data", series);
		}
		
		return result;
	}

	
	
	/**
	 * 柱形图数据处理
	 * 
	 * @param dateStr
	 * @param applayList
	 * @return
	 */
	private Long getContents(String dateStr, List<ApplyReportView> applayList) {
		Long total = 0L;
		for(ApplyReportView applyReport : applayList) {
			if(applyReport != null) {
				if (StringUtils.equals(dateStr, applyReport.getDates())) {
					total = applyReport.getTotal();
					break;
				}
			}
		}
		return total;
	}

	public static void main(String[] args) {
		Object[] strArray = new Object[9];
		Object[] str01Array = new Object[5];
		Random ra =new Random();
		for(int i=0;i<strArray.length;i++) {
			ra =new Random();
			str01Array[0] = "10-" + (i+1);
			str01Array[2] = "123";
			str01Array[1] = ra.nextFloat();
			str01Array[3] = ra.nextFloat();
			str01Array[4] = ra.nextFloat();
			strArray[i] = str01Array;
		}
		System.out.println(JSONArray.fromObject(strArray));
			
	}
	/**
	 * 支付统计
	 * 
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/payment/query")
	@ResponseBody
	public PaymentRecordRespView queryPaymentRecordReport(@RequestBody Map<String,Object> params) throws ParseException{
		String traderId = params.get("traderId").toString();
		PaymentRecordRespView result = new PaymentRecordRespView();
		List<Device> deviceList = deviceService.findByTraderId(traderId);
		//商家下面没有设备不进行报表展示
		if (CollectionUtils.isEmpty(deviceList)) {
			return result;
		} 
		String[] devices = new String[deviceList.size()];
		for(int i=0;i<deviceList.size();i++) {
			devices[i] = deviceList.get(i).getMachNo();
		}
		result.setDevices(devices);
		//X轴日期刻度处理
		String startTime = String.valueOf(params.get("startTime"));
		String endTime = String.valueOf(params.get("endTime"));
		String type = params.get("type").toString();
		SimpleDateFormat df = null;
		String format = null;
		if(StringUtils.isNotBlank(startTime) 
				&& StringUtils.isNotBlank(endTime)) {
			if (StringUtils.equals("day", type) || StringUtils.equals("week", type)) {
				format = "yyyy-MM-dd";
			} else if (StringUtils.equals("month", type)) {
				format = "yyyy-MM";
			} else if (StringUtils.equals("year", type)) {
				format = "yyyy";
			}
			df = new SimpleDateFormat(format);
			List<String> dateList = this.getDatesBetweenTwoDate(df.parse(startTime), df.parse(endTime),df,type);
			result.setDates(dateList.toArray(new String[dateList.size()]));
		}
		//Y轴
		List<PaymentRecordView>  paymentRecordList = paymentRecordReportService.findAllPaymentRecordReport(params);
		JSONArray jsonArray = new JSONArray();
		if (!CollectionUtils.isEmpty(paymentRecordList)) {
			JSONObject jsonObject = new JSONObject();
			Float[] totals = null;
			for (String device : devices) {
				jsonObject.put("name", device);
				jsonObject.put("type", "bar");
				jsonObject.put("barWidth", 25);
				jsonObject.put("stack", "支付统计");
				totals = getPaymentRecordSeriesTotal(device,paymentRecordList,result.getDates(),type);
				jsonObject.put("data", totals);
				jsonArray.add(jsonObject);
			}
			result.setSeries(jsonArray);
		}
		return result;
	}
	
	
	

	/**
	 * 扫码统计查询
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/sweepcode/query")
	@ResponseBody
	public SweepCodeReportRespView querySweepCodeReport(@RequestBody Map<String,Object> params) throws ParseException{
		String traderId = params.get("traderId").toString();
		SweepCodeReportRespView result = new SweepCodeReportRespView();
		List<Device> deviceList = deviceService.findByTraderId(traderId);
		//商家下面没有设备不进行报表展示
		if (CollectionUtils.isEmpty(deviceList)) {
			return result;
		} 
		String[] devices = new String[deviceList.size()];
		for(int i=0;i<deviceList.size();i++) {
			devices[i] = deviceList.get(i).getMachNo();
		}
		result.setDevices(devices);
		//X轴日期刻度处理
		String startTime = String.valueOf(params.get("startTime"));
		String endTime = String.valueOf(params.get("endTime"));
		String type = params.get("type").toString();
		SimpleDateFormat df = null;
		String format = null;
		if(StringUtils.isNotBlank(startTime) 
				&& StringUtils.isNotBlank(endTime)) {
			if (StringUtils.equals("day", type) || StringUtils.equals("week", type)) {
				format = "yyyy-MM-dd";
			} else if (StringUtils.equals("month", type)) {
				format = "yyyy-MM";
			} else if (StringUtils.equals("year", type)) {
				format = "yyyy";
			}
			df = new SimpleDateFormat(format);
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
				totals = getSweepCodeSeriesTotal(device,sweepCodeList,result.getDates(),type);
				jsonObject.put("data", totals);
				jsonArray.add(jsonObject);
			}
			result.setSeries(jsonArray);
			result.setTotal(getSweepCodeTotal(sweepCodeList));
			long invervalDay = getIntervalDay(startTime,endTime,format);
			result.setRate(getSweepCodeRate(invervalDay,devices.length,result.getTotal()));
		}
		
		return result;
	}
	
	private List<String> getDatesBetweenTwoDate(Date beginDate, Date endDate,SimpleDateFormat df,String type){
		 List<String> lDate = new ArrayList<String>();  
	        lDate.add(df.format(beginDate));// 把开始时间加入集合  
	        Calendar cal = Calendar.getInstance();  
	        // 使用给定的 Date 设置此 Calendar 的时间  
	        cal.setTime(beginDate);  
	        boolean bContinue = true;  
	        String date = null;
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
	        	date = df.format(cal.getTime());
	            if (endDate.after(cal.getTime())) {  
	                lDate.add(date);  
	            } else {  
	                break;  
	            }  
	        }  
	        if(endDate.getTime() != beginDate.getTime())
	        lDate.add(df.format(endDate));// 把结束时间加入集合  
	        return lDate;  
	} 
	
	private Float getSweepCodeRate(long invervalDay,int deviceTotal, Long total) {
		BigDecimal invervalDayCimal = new BigDecimal(invervalDay);
		BigDecimal deviceTotalCimal = new BigDecimal(deviceTotal);
		BigDecimal totalCimal = new BigDecimal(total);
		BigDecimal result = invervalDayCimal.multiply(deviceTotalCimal);
		result = totalCimal.divide(result,2,BigDecimal.ROUND_HALF_DOWN);
		result = result.multiply(new BigDecimal(100));
		Float sweepCodeRate = result.floatValue();
		if(100.00f < sweepCodeRate) {
			return 100.00f;
		}
		return sweepCodeRate;
	}
	
	/**
	 * 两日期相差天数计算
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 */
	private static long getIntervalDay(String startDate, String endDate, String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		long differ = 0;
		try {
			Calendar calEnd = Calendar.getInstance();
			if (StringUtils.equals(startDate, endDate)) {
				calEnd.setTime(df.parse(endDate));
				if (StringUtils.equals(format, "yyyy-MM")) {
					differ = calEnd.getActualMaximum(Calendar.DAY_OF_MONTH);
				} else if(StringUtils.equals(format, "yyyy")) {
					differ = calEnd.getActualMaximum(Calendar.DAY_OF_YEAR);
				} else {
					differ = 1;
				}
			} else {
				if (StringUtils.equals("yyyy-MM-dd", format) || StringUtils.equals(startDate, endDate)) {
					calEnd.setTime(df.parse(endDate));
				} 
				calEnd.add(Calendar.DATE, 1);
				differ = (calEnd.getTime().getTime() - df.parse(startDate).getTime()) / (24*60*60*1000);
			}
			return differ;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 扫码量计算
	 * 
	 * @param sweepCodeList
	 * @return
	 */
	private Long getSweepCodeTotal(List<SweepCodeReportView> sweepCodeList) {
		Long sweepCodeTotal = 0L;
		for (SweepCodeReportView sweepCode : sweepCodeList) {
			sweepCodeTotal+=sweepCode.getTotal();
		}
		return sweepCodeTotal;
	}

	/**
	 * 构造扫码统计series数据
	 * 
	 * @param device
	 * @param sweepCodeList
	 * @param dates
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	private Long[] getSweepCodeSeriesTotal(String device,List<SweepCodeReportView> sweepCodeList,String[] dates,String type) throws ParseException {
		List<Long> totals = new ArrayList<Long>();
		String seriesTotalStr = null;
		for (String d : dates) {
			if(StringUtils.equals("week", type)) {
				seriesTotalStr = getSeriesTotal(device, d, sweepCodeList, true);
			} else {
				seriesTotalStr = getSeriesTotal(device, d, sweepCodeList);
			}
			totals.add(Long.parseLong(seriesTotalStr.split(",")[1]));
		}
		return totals.toArray(new Long[totals.size()]);
	}
	
	/**
	 * 构造支付统计series数据
	 * @param device
	 * @param paymentRecordList
	 * @param dates
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	private Float[] getPaymentRecordSeriesTotal(String device, List<PaymentRecordView> paymentRecordList, String[] dates,
			String type) throws ParseException {
		List<Float> totals = new ArrayList<Float>();
		String seriesTotalStr = null;
		for (String d : dates) {
			if(StringUtils.equals("week", type)) {
				seriesTotalStr = getPaymentRecordSeriesTotals(device, d, paymentRecordList, true);
			} else {
				seriesTotalStr = getPaymentRecordSeriesTotals(device, d, paymentRecordList);
			}
			totals.add(Float.parseFloat(seriesTotalStr.split(",")[1]));
		}
		return totals.toArray(new Float[totals.size()]);
	}

	private String getPaymentRecordSeriesTotals(String device, String date, List<PaymentRecordView> paymentRecordList) {
		PaymentRecordView paymentRecord;
		String result = "";
		for (int i=0; i<paymentRecordList.size();i++) {
			paymentRecord = paymentRecordList.get(i);
			if (StringUtils.equals(date, paymentRecord.getDates()) && StringUtils.equals(device, paymentRecord.getMachno())) {
				result = device + "," + paymentRecord.getTotal();
				break;
			} 
		}
		if (StringUtils.isEmpty(result)) {
			result = device + "," + 0L;
		}
		return result;
	}

	private String getPaymentRecordSeriesTotals(String device, String date, List<PaymentRecordView> paymentRecordList,
			boolean b) throws ParseException {
		PaymentRecordView paymentRecord;
		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String paymentRecordDateStr = null;
		for (int i=0; i<paymentRecordList.size();i++) {
			paymentRecord = paymentRecordList.get(i);
			paymentRecordDateStr= paymentRecord.getDates();
			cal.setTime(df.parse(paymentRecordDateStr));
			cal.add(Calendar.DATE, 1);
			if (StringUtils.equals(date, df.format(cal.getTime())) && StringUtils.equals(device, paymentRecord.getMachno())) {
				result = device + "," + paymentRecord.getTotal();
				break;
			} 
		}
		if (StringUtils.isEmpty(result)) {
			result = device + "," + 0L;
		}
		return result;
	}

	/**
	 * 构造每个series数据
	 * 
	 * @param device
	 * @param date
	 * @param sweepCodeList
	 * @return
	 */
	private String getSeriesTotal(String device, String date, List<SweepCodeReportView> sweepCodeList) {
		SweepCodeReportView sweepCode;
		String result = "";
		for (int i=0; i<sweepCodeList.size();i++) {
			sweepCode = sweepCodeList.get(i);
			if (StringUtils.equals(date, sweepCode.getDates()) && StringUtils.equals(device, sweepCode.getMachno())) {
				result = device + "," + sweepCode.getTotal();
				break;
			} 
		}
		if (StringUtils.isEmpty(result)) {
			result = device + "," + 0L;
		}
		return result;
	}
	
	/**
	 * 构造每个series数据(周统计时需要特殊处理 date+1)
	 * 
	 * @param device
	 * @param date
	 * @param sweepCodeList
	 * @return
	 * @throws ParseException 
	 */
	private String getSeriesTotal(String device, String date, List<SweepCodeReportView> sweepCodeList, boolean isweek) throws ParseException {
		SweepCodeReportView sweepCode;
		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String sweepDateStr = null;
		for (int i=0; i<sweepCodeList.size();i++) {
			sweepCode = sweepCodeList.get(i);
			sweepDateStr= sweepCode.getDates();
			cal.setTime(df.parse(sweepDateStr));
			cal.add(Calendar.DATE, 1);
			if (StringUtils.equals(date, df.format(cal.getTime())) && StringUtils.equals(device, sweepCode.getMachno())) {
				result = device + "," + sweepCode.getTotal();
				break;
			} 
		}
		if (StringUtils.isEmpty(result)) {
			result = device + "," + 0L;
		}
		return result;
	}

}
