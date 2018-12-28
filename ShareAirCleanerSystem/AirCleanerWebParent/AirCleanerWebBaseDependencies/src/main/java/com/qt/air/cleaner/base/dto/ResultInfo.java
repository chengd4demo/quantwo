package com.qt.air.cleaner.base.dto;

import java.io.Serializable;

public class ResultInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String status;

	private String description;

	private Object data;

	public ResultInfo() {
	}

	public ResultInfo(String status, String description, Object data) {
		this.status = status;
		this.description = description;
		this.data = data;
	}
	
	public ResultInfo(String status, String description) {
		super();
		this.status = status;
		this.description = description;
	}

	@Override
	public String toString() {
		return "ResultInfo{" + "status='" + status + '\'' + ", description='" + description + '\'' + ", data=" + data
				+ '}';
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
