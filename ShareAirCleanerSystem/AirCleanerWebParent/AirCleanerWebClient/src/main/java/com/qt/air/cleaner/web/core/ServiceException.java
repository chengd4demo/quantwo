/**     
 * @Title: ServiceException.java   
 * @Package com.qt.air.cleaner.core   
 * @Description: 异常封装
 * @author MaBin 
 * @date 2017年8月10日 下午4:11:13   
 * @version V1.0     
 */
package com.qt.air.cleaner.web.core;

/**
 * @Title: ServiceException.java
 * @Description: 异常处理
 * @author MaBin
 * @date 2017年8月10日 下午4:11:13
 * @version V1.0
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -9046362194677293871L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
