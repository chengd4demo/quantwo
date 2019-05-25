package com.qt.air.cleaner.web.customer.web;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.web.customer.service.PayService;

@RestController
@RequestMapping("/pay-channel")
public class PayChannelConstroller {
	private static Logger logger = LoggerFactory.getLogger(PayChannelConstroller.class);
	/**注入支付接口**/
	@Autowired
	PayService payService;
	@Value("${o2.system.domain}")
	public String systeDomain;
	@GetMapping("/pay")
	public ResultInfo payAuthorization(HttpServletRequest request,HttpServletResponse response) {
		String machNo = request.getParameter("machNo");
		String priceId = request.getParameter("priceId");
		String ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR") == null ? request.getRemoteAddr() : request.getHeader("HTTP_X_FORWARDED_FOR");
		logger.info("execute pay-channel's method payAuthorization()  start -> param:{machNo: "+machNo+",priceId: "+priceId+"}");	
		logger.info("ipAddress -> " + ipAddress);
		try {
			if(StringUtils.isNotBlank(machNo) && StringUtils.isNotBlank(priceId)) {
				RequestParame parames = new RequestParame();
				parames.setData(new HashMap<String,String>(){
					{
					 put("payType","WX");
					 put("priceId",priceId);
					 put("deviceId",machNo);
					 put("ipAddress",ipAddress);
					}
				});
				return payService.payAuth(parames);
			} else {
				return new ResultInfo(ResultCode.R2001.code,ResultCode.R2001.info,null);
			}
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	@GetMapping("create/billing")
	public ResultInfo createBilling(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("deviceId:" + request.getParameter("deviceId"));
		try {
			RequestParame parames = new RequestParame();
			parames.setData(new HashMap<String, String>() {
				{
					put("code", request.getParameter("code"));
					put("priceId", request.getParameter("priceId"));
					put("deviceId", request.getParameter("deviceId"));
					put("sign", request.getParameter("sign"));
					put("ipAddress", request.getParameter("ipAddress"));
				}
			});
			logger.info("execute pay-channel's method createBilling()  start ->param:{}",new Gson().toJson(parames));	
			ResultInfo result = payService.createBilling(parames);
			if(result != null && StringUtils.equals(result.getStatus(), String.valueOf(ResultCode.SC_OK))){
				logger.info("createBilling backUrl:{}",result.getData());
				response.sendRedirect(result.getData().toString());
			}
			return null;
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	@GetMapping("complete/billing")
	public ResultInfo completeBilling(HttpServletRequest request,HttpServletResponse response) {
		try {
			RequestParame parames = new RequestParame();
			parames.setData(new HashMap<String, String>() {
				{
					put("code", request.getParameter("code"));
					put("priceId", request.getParameter("priceid"));
					put("deviceId", request.getParameter("deviceid"));
					put("sign", request.getParameter("sign"));
					put("ipAddress", request.getParameter("ipaddress"));
				}
			});
			logger.info("execute pay-channel's method completeBilling()  start ->param:{}",new Gson().toJson(parames));
			ResultInfo result = payService.completeBilling(parames);
			String url = systeDomain;
			if(result != null && StringUtils.equals(result.getStatus(), String.valueOf(ResultCode.SC_OK))){
				logger.info("jspai配置参数：{}",result.getData().toString());
				String queryString = result.getData().toString();
				queryString = queryString.substring(1, queryString.length()-1).replaceAll(", ", "&");
				url += "?jsapi=config&"+queryString;
			}
			logger.info("jsapi重定向URL:{}",url);
			response.sendRedirect(url);
			return null;
		} catch (Exception e) {
			logger.error("system error: {}",e.getMessage());
			e.printStackTrace();
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	
	/**
	 * 微信支付回掉通知
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	@PostMapping("billing/notify")
	public String billingNotify(HttpServletRequest request) {
		logger.info("execute pay-channel's method billingNotify()  start");
		try {
			InputStream inStream = request.getInputStream();
			int _buffer_size = 1024;
			if (inStream != null) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] tempBytes = new byte[_buffer_size];
				int count = -1;
				while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
					outStream.write(tempBytes, 0, count);
				}
				tempBytes = null;
				outStream.flush();
				String strXML = new String(outStream.toByteArray(), "UTF-8");
				
				logger.info("微信通知内容：" + strXML);
			    ResultInfo result = payService.billingNotify(strXML);
			    if (result != null && StringUtils.equals(result.getStatus(), String.valueOf(ResultCode.SC_OK))){
			    	 return "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
			 		        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
			    }
			}
			return "fail";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("system error: {}",e.getMessage());
			return "fail";
		}
	}
	
	@PostMapping("wx/msg")
	public ResultInfo weiXinMsg(@RequestParam("type") String type,@RequestParam("billingNumber") String billingNumber) {
		logger.info("execute pay-channel's method weiXinMsg()  start");
		try {
			logger.info("支付异常信息处理{}类型{}", billingNumber, type);
			return payService.weiXinMsg(type, billingNumber);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("system error: {}",e.getMessage());
			return new ResultInfo(String.valueOf(ResultCode.R5001.code),e.getMessage(), null);
		}
	}
	
	
}
