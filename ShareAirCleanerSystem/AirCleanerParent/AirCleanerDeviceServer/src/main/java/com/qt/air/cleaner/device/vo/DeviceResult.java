package com.qt.air.cleaner.device.vo;

public class DeviceResult {
public final static Integer ON_LINE_ENABLE = 1;
	
	public final static Integer ON_LINE_DISABLE = 0;
	
	public final static Integer TURN_ON_TRUE = 1;
	
	public final static Integer TURN_ON_FALSE = 0;
	
	private Integer online;
	
	private Integer turnOn;
	
	private Integer pm25;
	
	private Integer pir;
	
	private Integer result;
	
	public Integer getOnline() {
		
		return online;
	}
	
	public void setOnline(Integer online) {
		
		this.online = online;
	}
	
	public Integer getTurnOn() {
		
		return turnOn;
	}
	
	public void setTurnOn(Integer turnOn) {
		
		this.turnOn = turnOn;
	}
	
	public Integer getPm25() {
		
		return pm25;
	}
	
	public void setPm25(Integer pm25) {
		
		this.pm25 = pm25;
	}
	
	public Integer getPir() {
		
		return pir;
	}
	
	public void setPir(Integer pir) {
		
		this.pir = pir;
	}
	
	public Integer getResult() {
		
		return result;
	}
	
	public void setResult(Integer result) {
		
		this.result = result;
	}
	
	@Override
	public String toString() {
		
		return "DeviceResult [online=" + online + ", turnOn=" + turnOn + ", pm25=" + pm25 + ", pir=" + pir + ", result="
		        + result + "]";
	}
}
