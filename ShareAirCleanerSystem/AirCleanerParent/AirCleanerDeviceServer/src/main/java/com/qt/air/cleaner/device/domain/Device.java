
package com.qt.air.cleaner.device.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "MK_DEVICE")
public class Device extends GenericEntity {
	
	public static final Integer ENABLED = 1;
	
	public static final Integer DISABLED = 0;
	
	public static final Integer UNSTATE = -1;
	
	private static final long serialVersionUID = -4374038665394112195L;
	
	@Column(name = "MACH_NO", nullable = false, length = 255, unique = true, insertable = true, updatable = false)
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
	
	@OneToOne
	private PriceSystem priceSystem;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Company company;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Investor investor;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Trader trader;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Saler saler;
	
	@Transient
	private String useState;
	
	
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
	
	public PriceSystem getPriceSystem() {
		
		return priceSystem;
	}

	public void setPriceSystem(PriceSystem priceSystem) {
		
		this.priceSystem = priceSystem;
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

	public String getUseState() {
		return useState;
	}

	public void setUseState(String useState) {
		this.useState = useState;
	}

	public String getDeviceSequence() {
		return deviceSequence;
	}

	public void setDeviceSequence(String deviceSequence) {
		this.deviceSequence = deviceSequence;
	}
}
