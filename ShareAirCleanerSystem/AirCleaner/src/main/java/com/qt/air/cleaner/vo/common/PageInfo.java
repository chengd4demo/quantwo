package com.qt.air.cleaner.vo.common;

import java.io.Serializable;
import java.util.List;

public class PageInfo<T> implements Serializable  {
	private static final long serialVersionUID = -9177270774288251825L;
	private int code;
	private String msg;
	private List<T> data;
	private long count;
	
	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public List<T> getData() {
		return data;
	}

	public long getCount() {
		return count;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public void setCount(long count) {
		this.count = count;
	}
	public PageInfo(int code, String msg, List<T> data, long count) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.count = count;
	}
	
}
