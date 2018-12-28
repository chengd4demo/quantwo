package com.qt.air.cleaner.base.exception;

import com.qt.air.cleaner.base.enums.ErrorCodeEnum;

public class BusinessRuntimeException extends Exception {
	private static final long serialVersionUID = -8664813748099152957L;

	/**  
	* 创建一个新的实例 BusinessRuntimeException.  
	*    
	*/
	public BusinessRuntimeException() {
	}
	
	/**
	 * @Fields field:错误码
	 */
	private String errorCode;
	
	/**  
	* 创建一个新的实例 BusinessRuntimeException.  
	*  
	* @param message  
	*/
	public BusinessRuntimeException(String message) {
		super(message);
	}

	/**  
	* 创建一个新的实例 BusinessRuntimeException.  
	*  
	* @param message
	* @param cause  
	*/
	public BusinessRuntimeException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**  
	* 创建一个新的实例 BusinessRuntimeException.  
	*  
	* @param message
	* @param cause
	* @param enableSuppression
	* @param writableStackTrace  
	*/
	public BusinessRuntimeException(ErrorCodeEnum errorCodeEnum) {
		super(errorCodeEnum.getMessage());
		this.errorCode = errorCodeEnum.getErrorCode();
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
