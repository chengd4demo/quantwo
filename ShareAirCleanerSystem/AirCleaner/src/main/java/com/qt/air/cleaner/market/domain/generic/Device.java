
package com.qt.air.cleaner.market.domain.generic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qt.air.cleaner.market.domain.price.PriceSystem;
import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "MK_DEVICE")
public class Device extends GenericEntity {
	
	public static final Integer ENABLED = 1;
	
	public static final Integer DISABLED = 0;
	
	public static final Integer UNSTATE = -1;
	
	private static final long serialVersionUID = -4374038665394112195L;
	
	@Column(name = "MACH_NO", nullable = false, length = 255, unique = true, insertable = true, updatable = true)
	private String machNo;
	
	@Column(name = "LOT", nullable = true, length = 255)
	private String lot;
	
	@Column(name = "SETUP_TIME", nullable = true, insertable = true, updatable = true)
	private String setupTime;
	
	@Column(name = "SETUP_ADDRESS", nullable = false, length = 255)
	private String setupAddress;
	
	@Column(name = "STATE", nullable = true, length = 1)
	private Integer state;
	
	@Column(name = "DEVICE_SEQUENCE", nullable = true, length = 15)
	private String deviceSequence;
	
	@Column(name = "DISTRIBUTION_RATIO", nullable = true, length = 255)
	private String distributionRatio;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Company company;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Investor investor;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Trader trader;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Saler saler;
	
	@OneToOne
	private PriceSystem priceSystem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private DeviceBatch deviceBatch;
	
	public String getMachNo() {
		
		return machNo;
	}
	
	public void setMachNo(String machNo) {
		
		this.machNo = machNo;
	}
	
	public String getLot() {
		
		return lot;
	}
	
	public void setLot(String lot) {
		
		this.lot = lot;
	}
	
	public String getSetupTime() {
		
		return setupTime;
	}
	
	public void setSetupTime(String setupTime) {
		
		this.setupTime = setupTime;
	}
	
	public String getSetupAddress() {
		
		return setupAddress;
	}
	
	public void setSetupAddress(String setupAddress) {
		
		this.setupAddress = setupAddress;
	}
	
	public Integer getState() {
		
		return state;
	}
	
	public void setState(Integer state) {
		
		this.state = state;
	}
	
	public String getDeviceSequence() {
		return deviceSequence;
	}

	public void setDeviceSequence(String deviceSequence) {
		this.deviceSequence = deviceSequence;
	}

	public Company getCompany() {
		
		return company;
	}
	
	public void setCompany(Company company) {
		
		this.company = company;
	}
	
	public Investor getInvestor() {
		
		return investor;
	}
	
	public void setInvestor(Investor investor) {
		
		this.investor = investor;
	}
	
	public Trader getTrader() {
		
		return trader;
	}
	
	public void setTrader(Trader trader) {
		
		this.trader = trader;
	}
	
	public Saler getSaler() {
		
		return saler;
	}
	
	public void setSaler(Saler saler) {
		
		this.saler = saler;
	}
	
	public PriceSystem getPriceSystem() {
		
		return priceSystem;
	}
	
	public DeviceBatch getDeviceBatch() {
		return deviceBatch;
	}

	public void setDeviceBatch(DeviceBatch deviceBatch) {
		this.deviceBatch = deviceBatch;
	}

	public void setPriceSystem(PriceSystem priceSystem) {
		
		this.priceSystem = priceSystem;
	}

	public String getDistributionRatio() {
		return distributionRatio;
	}

	public void setDistributionRatio(String distributionRatio) {
		this.distributionRatio = distributionRatio;
	}
}
