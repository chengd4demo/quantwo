package com.qt.air.cleaner.vo.common;

import java.io.Serializable;

import net.sf.json.JSONObject;


/**
 * @ClassName: RetResult
 * @Description: TODO
 * @author MaBin
 * @date 2017年8月10日 下午4:09:49
 * 
 */
public class RetResult<T> implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 3758864789222317092L;

	public int code;

	private String msg;

	private T data;

	public RetResult<T> setCode(RetCode retCode) {
		this.code = retCode.code;
		return this;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @Title: setCode
	 * @Description: TODO
	 * @param code
	 * @Reutrn RetResult
	 */
	public RetResult<T> setCode(int code) {
		this.code = code;
		return this;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @Title: setMsg
	 * @Description: TODO
	 * @param msg
	 * @Reutrn RetResult
	 */
	public RetResult<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	/**
	 * @return the obj
	 */
	public T getData() {
		return data;
	}

	/**
	 * @Title: setObj
	 * @Description: TODO
	 * @param data
	 * @Reutrn RetResult
	 */
	public RetResult<T> setData(T data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
}
