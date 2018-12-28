package com.qt.air.cleaner.base.dto;

import java.io.Serializable;

public class RequestDto implements Serializable{
	private static final long serialVersionUID = -3505398316098987875L;
	private Head head;
	private Object body;
	public Head getHead() {
		return head;
	}
	public Object getBody() {
		return body;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "RequestDto [head=" + head + ", body=" + body + "]";
	}
	
}
