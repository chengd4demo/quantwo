package com.qt.air.cleaner.base.utils;

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
	
	/**
	 * 投资商分润金额计算
	 * @param totalAmount
	 * @param proportion
	 * @return
	 */
	public static Float getInvestorAmount(Float totalAmount,Integer proportion,Integer costTime,Float free){
		BigDecimal b1 = new BigDecimal(Float.toString(totalAmount));
		Float result = b1.multiply(new BigDecimal(Integer.toString(proportion))).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		BigDecimal freeBigDecimal = new BigDecimal(Float.toString(free));
		free = freeBigDecimal.multiply(new BigDecimal(costTime)).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		// 当计算金额大于耗材费用 才扣除耗材费用
		if (result>free) {
			result = new BigDecimal(Float.toString(result)).subtract(new BigDecimal(Float.toString(free))).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		}
		return result;
	}
	
	/**
	 * 公司分润金额计算
	 * 
	 * @param totalAmount
	 * @param proportion
	 * @return
	 */
	public static Float getCompanyAmount(Float totalAmount,Integer proportion) {
		return getShareAmount(totalAmount, proportion);
	}
	
	/**
	 * 投资商分润金额计算
	 * 
	 * @param totalAmount
	 * @param proportion
	 * @return
	 */
	public static Float getTraderAmount(Float totalAmount,Integer proportion) {
		return getShareAmount(totalAmount, proportion);
	}
	
	
	/**
	 * 代理商分润金额计算
	 * 
	 * @param totalAmount
	 * @param proportion
	 * @return
	 */
	public static Float getAgentAmount(Float totalAmount,Integer proportion) {
		return getShareAmount(totalAmount, proportion);
	}
	
	
	private static Float getShareAmount(Float totalAmount,Integer proportion) {
		return new BigDecimal(Float.toString(totalAmount)).multiply(new BigDecimal(Integer.toString(proportion)))
				.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
	}
	
}
