package com.qt.air.cleaner.market.vo.generic;


import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DeviceExcel implements Serializable {
	private static final long serialVersionUID = 7108750029366291257L;
	@Excel(name = "设备编码")
	private String machNo;
	@Excel(name = "安装地址")
	private String setupAddress;
	public DeviceExcel(String machNo, String setupAddress) {
		this.machNo = machNo;
		this.setupAddress = setupAddress;
	}
	public DeviceExcel() {
	}

	public String getMachNo() {
		return machNo;
	}
	public String getSetupAddress() {
		return setupAddress;
	}
	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}
	public void setSetupAddress(String setupAddress) {
		this.setupAddress = setupAddress;
	}
}
