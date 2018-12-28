package com.qt.air.cleaner.base.enums;

public enum AccountOutBoundEnum {
	REQUEST("已申请",0),UNCOLLECTED("未领取",1),CANCELLED("已取消",2),COMPLETED("已完成",3),NOTPASS("未通过",4);
	// 构造方法
	private AccountOutBoundEnum(String name, int status) {
		this.name = name;
		this.status = status;
	}
	private String name;
	private int status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
