package com.qt.air.cleaner.web.merchant.service;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qt.air.cleaner.base.dto.Head;

@Component
public class TokenServiceImpl implements TokenService {
	
	@Value("${token.partner}")
	private String partner;
	@Value("${token.key}")
	private String key;
	
	@SuppressWarnings("rawtypes")
	@Override
	public String getToken(String inPartner) {
		if (StringUtils.isNotBlank(partner) && StringUtils.equals(inPartner, partner)) {
			Map<String, String> keyMap = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			keyMap.put("partner", partner);
			keyMap.put("key", key);
			StringBuilder sb = new StringBuilder();
			for (Entry entry : keyMap.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");// 拼接字符串
			}
			String linkString = sb.toString();
			linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);
			Head head = new Head(partner, key, DigestUtils.md5Hex(linkString + key), DigestUtils.md5Hex(linkString + key));
			String token = null;
			try {
				token = this.convertObjectToJson(head);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return token;
		}
		return null;
	}
	
	private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
