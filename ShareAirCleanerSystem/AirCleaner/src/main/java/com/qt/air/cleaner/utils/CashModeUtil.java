package com.qt.air.cleaner.utils;

import java.util.HashMap;
import java.util.Map;

import com.singalrain.framework.util.StringUtils;

public class CashModeUtil {
	public static Map<Integer, String> cashMode = new HashMap<Integer, String>();
	static {
		cashMode.put(0, "红包");
		cashMode.put(1, "转账");
		cashMode.put(2, "退款");
	}
	public static String getCashMode(Integer code) {
		String name = cashMode.get(code);
		if (StringUtils.isBlank(name)) {
			name = "未知";
		}
		return name;
	}
}
