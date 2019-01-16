
package com.qt.air.cleaner.scheduled.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qt.air.cleaner.base.domain.GenericEntity;


@Entity
@Table(name = "ACT_ACCOUNT_INBOUND")
public class AccountInBound extends GenericEntity {
	
	public final static Integer ACCOUNT_IN_BOUND_WAIT = 0;
	
	public final static Integer ACCOUNT_IN_BOUND_CONFIRM = 1;
	
	private static final long serialVersionUID = 6110286839940381033L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account account;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Billing billing;
	
	@Column(name = "TYPE", length = 255, nullable = false, insertable = true, updatable = false)
	private String type;
	
	@Column(name = "CODE", length = 255, nullable = false, insertable = true, updatable = false)
	private String code;
	
	@Column(name = "NAME", length = 255, nullable = false, insertable = true, updatable = false)
	private String name;
	
	@Column(name = "AMOUNT", precision = 10, scale = 2, nullable = false, insertable = true, updatable = false)
	private Float amount;
	
	@Column(name = "WEIXIN", nullable = false, length = 40)
	private String weixin;
	
	@Column(name = "STATE")
	private Integer state = ACCOUNT_IN_BOUND_WAIT;
	
	public Account getAccount() {
		
		return account;
	}
	
	public void setAccount(Account account) {
		
		this.account = account;
	}
	
	public Billing getBilling() {
		
		return billing;
	}
	
	public void setBilling(Billing billing) {
		
		this.billing = billing;
	}
	
	public Float getAmount() {
		
		return amount;
	}
	
	public void setAmount(Float amount) {
		
		this.amount = amount;
	}
	
	public Integer getState() {
		
		return state;
	}
	
	public void setState(Integer state) {
		
		this.state = state;
	}
	
	public String getType() {
		
		return type;
	}
	
	public void setType(String type) {
		
		this.type = type;
	}
	
	public String getCode() {
		
		return code;
	}
	
	public void setCode(String code) {
		
		this.code = code;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public String getWeixin() {
		
		return weixin;
	}
	
	public void setWeixin(String weixin) {
		
		this.weixin = weixin;
	}
}
