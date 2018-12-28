package com.qt.air.cleaner.device.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.device.vo.DeviceResult;

import net.sf.json.JSONObject;

@Component
public class DeviceUtil {
	private static Logger logger = LoggerFactory.getLogger(DeviceUtil.class);

	public static String deviceURI;

	public static String queryMethod;

	public static String turnOffMethod;

	public static String turnOnMethod;

	public static DeviceResult queryDeviceState(String deviceId) {

		String url = deviceURI + queryMethod;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("devid", deviceId);
		String queryResult = queryUriResult(url, requestParams);
		JSONObject object = JSONObject.fromObject(queryResult);
		DeviceResult result = new DeviceResult();
		result.setOnline(object.getInt("online"));
		result.setPir(object.getInt("pir"));
		result.setPm25(object.getInt("pm25"));
		result.setTurnOn(object.getInt("turn_on"));
		result.setResult(object.getInt("result"));
		return result;
	}

	public static Integer turnOnDevice(String deviceId, Integer sec) {

		String url = deviceURI + turnOnMethod;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("devid", deviceId);
		requestParams.put("sec", sec.toString());
		String queryResult = queryUriResult(url, requestParams);
		JSONObject object = JSONObject.fromObject(queryResult);
		return object.getInt("result");
	}

	public static Integer turnOffDevice(String deviceId) {

		String url = deviceURI + turnOffMethod;
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("devid", deviceId);
		String queryResult = queryUriResult(url, requestParams);
		JSONObject object = JSONObject.fromObject(queryResult);
		return object.getInt("result");
	}

	@Value("${device.uri}")
	public void setDeviceURI(String deviceURI) {

		DeviceUtil.deviceURI = deviceURI;
	}

	@Value("${device.query.method}")
	public void setQueryMethod(String queryMethod) {

		DeviceUtil.queryMethod = queryMethod;
	}
	@Value("${device.turn-off.method}")
	public void setTurnOffMethod(String turnOffMethod) {

		DeviceUtil.turnOffMethod = turnOffMethod;
	}
	@Value("${device.turn-on.method}")
	public void setTurnOnMethod(String turnOnMethod) {

		DeviceUtil.turnOnMethod = turnOnMethod;
	}

	private static String queryUriResult(String url, Map<String, String> params) {

		String queryResult = "";
		try {

			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000)
					.setConnectionRequestTimeout(5000).build();
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			List<NameValuePair> formParams = buildUrlRequestParams(params);
			if (formParams != null && !formParams.isEmpty()) {
				HttpEntity requestEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
				post.setEntity(requestEntity);
			}
			post.setConfig(requestConfig);
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity responseEntity = response.getEntity();
				queryResult = EntityUtils.toString(responseEntity, "utf-8");
			}
		} catch (ParseException | IOException ex) {
			logger.error("请求地址" + url + "发生错误!", ex);
		}
		return queryResult;
	}

	private static List<NameValuePair> buildUrlRequestParams(Map<String, String> params) {

		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		if (null != params && !params.isEmpty()) {
			for (Map.Entry<String, String> query : params.entrySet()) {
				String key = query.getKey();
				String value = query.getValue();
				if (StringUtils.isNotBlank(value)) {
					formParams.add(new BasicNameValuePair(key, value));
				}
			}
		}
		return formParams;
	}
}
