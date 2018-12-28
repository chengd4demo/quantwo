package com.qt.air.cleaner.pay.utils;

import java.security.MessageDigest;

public class SHA1Util {
	private static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;// 42
		StringBuilder buf = new StringBuilder(len * 2);// 43

		for (int j = 0; j < len; ++j) {// 45
			buf.append(HEX_DIGITS[bytes[j] >> 4 & 15]);// 46
			buf.append(HEX_DIGITS[bytes[j] & 15]);// 47
		}

		return buf.toString();// 49
	}

	public static String encode(String str) {
		if (str == null) {// 54
			return null;
		} else {
			try {
				MessageDigest e = MessageDigest.getInstance("SHA1");// 56
				e.update(str.getBytes());// 57
				return getFormattedText(e.digest());// 58
			} catch (Exception arg1) {// 59
				throw new RuntimeException(arg1);// 60
			}
		}
	}
}
