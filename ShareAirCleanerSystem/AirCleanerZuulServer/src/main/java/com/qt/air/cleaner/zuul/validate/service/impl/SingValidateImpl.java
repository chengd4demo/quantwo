package com.qt.air.cleaner.zuul.validate.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.zuul.dto.Head;
import com.qt.air.cleaner.zuul.validate.service.SignValidateService;

@Component
public class SingValidateImpl implements SignValidateService {
	private final Logger logger = LoggerFactory.getLogger(SingValidateImpl.class);
	@Value("${token.partner}")
	private String partner;
	@Value("${token.key}")
	private String key;
	

	@Override
	public boolean tokenValidate(Head head) throws BusinessRuntimeException {
		String token = head.getToken();
		if (StringUtils.isEmpty(token)) {
			throw new BusinessRuntimeException(String.valueOf(ResultCode.SC_UNAUTHORIZED), ResultCode.R0000.info);
		} 
 		return this.signValidate(head);
	}

	private boolean signValidate(Head head) {
		String key = head.getKey();
		String sign = head.getMdkey();
		
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(head.getMdkey()) || StringUtils.isEmpty(head.getPartner()) ) {
			return false;
		}
		Map<String, Object> parameterMap = null;
		try {
			parameterMap = this.objectToMap(head);
		} catch (Exception e) {
			logger.error("签名验证异常");
		}
		List<String> keys = new ArrayList<>(parameterMap.keySet());
		keys.remove("mdkey");
		keys.remove("token");
		Collections.sort(keys);// 排序
		StringBuilder sb = new StringBuilder();
		for (String parameKey : keys) {
			sb.append(parameKey).append("=").append(parameterMap.get(parameKey)).append("&");// 拼接字符串
		}
		String linkString = sb.toString();
		linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);
		String secret = key;
		return StringUtils.equals(sign, DigestUtils.md5Hex(linkString + secret));
	}
	
	private Map<String,Object> objectToMap(Object obj) throws  Exception{
		if(obj == null)  
			return null;
		Map<String,Object> map = new HashMap<String,Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors(); 
		for (PropertyDescriptor property : propertyDescriptors) {
			 String key = property.getName();
			 if (key.compareToIgnoreCase("class") == 0) {
				 continue;  
			 }
			 Method getter = property.getReadMethod();
			 Object value = getter!=null ? getter.invoke(obj) : null;  
			 map.put(key, value);
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getToken(String partner, String key) {
		if (StringUtils.isNotBlank(partner) && StringUtils.isNotBlank(key)) {
			Map<String, String> keyMap = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			keyMap.put("partner", partner);
			keyMap.put("key", key);
			List<String> keys = new ArrayList<>();
			keys.add("key");
			Collections.sort(keys);// 排序
			StringBuilder sb = new StringBuilder();
			for (Entry entry : keyMap.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");// 拼接字符串
			}
			String linkString = sb.toString();
			linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);
			return DigestUtils.md5Hex(linkString + key);
		}
		return key;
	}
	
}
