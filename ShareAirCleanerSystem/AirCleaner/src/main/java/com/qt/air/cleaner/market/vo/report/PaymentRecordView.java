package com.qt.air.cleaner.market.vo.report;

import java.io.Serializable;

public class PaymentRecordView implements Serializable {
	private static final long serialVersionUID = -873822625055477143L;
	private String dates;
	private Float total = 0.00f;
	private String machno;	
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public String getMachno() {
		return machno;
	}
	public void setMachno(String machno) {
		this.machno = machno;
	}
}
