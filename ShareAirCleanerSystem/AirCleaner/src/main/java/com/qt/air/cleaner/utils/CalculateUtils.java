package com.qt.air.cleaner.utils;

import java.math.BigDecimal;

public class CalculateUtils {
	
	/**
	 * 加法运算
	 * @param var1
	 * @param var2
	 * @return
	 */
	public static Float add(Float var1, Float var2) {
		BigDecimal b1 = new BigDecimal(Float.toString(var1));
		BigDecimal b2 = new BigDecimal(Float.toString(var2));
		return b1.add(b2).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
	}
	
	/**
	 * 
	 * @param var1
	 * @param var2
	 * @return
	 */
	public static Float sub(Float var1, Float var2) {
		BigDecimal b1 = new BigDecimal(Float.toString(var1));
		BigDecimal b2 = new BigDecimal(Float.toString(var2));
		return b1.subtract(b2).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
	}
	
}
