package com.qt.air.cleaner.scheduled.vo;

import java.io.Serializable;

public class PaymentRecordReportView implements Serializable {
	private static final long serialVersionUID = -7556140535491184630L;
	private Float amount = 0.00f;
	private String sweepcodetime;
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	private String machno;
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
