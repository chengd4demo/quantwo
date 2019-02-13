package com.qt.air.cleaner.market.vo.report;

import java.io.Serializable;

public class ApplyReportView implements Serializable {
	private static final long serialVersionUID = 4941974940744352479L;
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
