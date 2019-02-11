package com.qt.air.cleaner.scheduled.vo;

import java.io.Serializable;

public class SweepCodeReportView implements Serializable {
	private static final long serialVersionUID = -2610263156266660005L;
	private Long total = 0L;
	private String sweepcodetime;
	private String machno;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public String getSweepcodetime() {
		return sweepcodetime;
	}
	public void setSweepcodetime(String sweepcodetime) {
		this.sweepcodetime = sweepcodetime;
	}
	public String getMachno() {
		return machno;
	}
	public void setMachno(String machno) {
		this.machno = machno;
	}
}
