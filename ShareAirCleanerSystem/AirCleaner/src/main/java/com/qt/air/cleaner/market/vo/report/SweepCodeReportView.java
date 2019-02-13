package com.qt.air.cleaner.market.vo.report;

import java.io.Serializable;

public class SweepCodeReportView implements Serializable{
	private static final long serialVersionUID = -910097643596728057L;
	private String dates;
	private Long total = 0L;
	private String machno;
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public String getMachno() {
		return machno;
	}
	public void setMachno(String machno) {
		this.machno = machno;
	}
}
