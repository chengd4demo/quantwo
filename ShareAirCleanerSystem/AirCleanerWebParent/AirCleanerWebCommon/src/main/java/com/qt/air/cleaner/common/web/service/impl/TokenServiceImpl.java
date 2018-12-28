package com.qt.air.cleaner.common.web.service.impl;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenServiceImpl implements TokenService {
	
	@Value("${token.partner}")
	private String partner;
	@Value("${token.key}")
	private String key;
	
	@SuppressWarnings("rawtypes")
	@Override
	public String getToken(String openId) {
		if (StringUtils.isNotBlank(partner) && StringUtils.isNotBlank(key)) {
			Map<String, String> keyMap = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			keyMap.put("openId", openId);
			keyMap.put("partner", partner);
			keyMap.put("key", key);
			StringBuilder sb = new StringBuilder();
			for (Entry entry : keyMap.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");// 拼接字符串
			}
			String linkString = sb.toString();
			linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);
			return DigestUtils.md5Hex(linkString + key);
		}
		return null;
	}
}
