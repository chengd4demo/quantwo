
package com.qt.air.cleaner.market.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;

import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "ACT_ACCOUNT", indexes = {
        @Index(columnList = "TYPE", name = "accountType")
})
public class Account extends GenericEntity {
	
	//代理
	public final static String ACCOUNT_TYPE_AGENT_DL = "DL";
	public final static String ACCOUNT_TYPE_AGENT_ZD = "ZD";
	//商家
	public final static String ACCOUNT_TYPE_TRADER = "TR";
	//投资商
	public final static String ACCOUNT_TYPE_INVESTOR = "IR";
	//公司
	public final static String ACCOUNT_TYPE_COMPANY = "CY";
	//促销员
	public final static String ACCOUNT_TYPE_SALER = "SR";
	//消费者
	public final static String ACCOUNT_TYPE_CUSTOMER = "CR";
	
	private static final long serialVersionUID = 3011122636062608081L;
	
	@Version
	private Integer version = 0;
	
	@Column(name = "TYPE", length = 2, nullable = false)
	private String accountType;
	
	@Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2, nullable = false)
	private Float totalAmount = 0.00f;
	
	@Column(name = "FREEZING_AMOUNT", precision = 10, scale = 2, nullable = false)
	private Float freezingAmount = 0.00f;
	
	@Column(name = "AVAILABLE_AMOUNT", precision = 10, scale = 2, nullable = false)
	private Float availableAmount = 0.00f;
	
	public Integer getVersion() {
		
		return version;
	}
	
	public void setVersion(Integer version) {
		
		this.version = version;
	}
	
	public String getAccountType() {
		
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		
		this.accountType = accountType;
	}
	
	public Float getTotalAmount() {
		
		return totalAmount;
	}
	
	public void setTotalAmount(Float totalAmount) {
		
		this.totalAmount = totalAmount;
	}
	
	public Float getFreezingAmount() {
		
		return freezingAmount;
	}
	
	public void setFreezingAmount(Float freezingAmount) {
		
		this.freezingAmount = freezingAmount;
	}
	
	public Float getAvailableAmount() {
		
		return availableAmount;
	}
	
	public void setAvailableAmount(Float availableAmount) {
		
		this.availableAmount = availableAmount;
	}
	
}
