package com.qt.air.cleaner.system.domain.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name="T_SYS_CONFIG")
public class SysConfig{
	@Id
	@GeneratedValue(generator = "guid")
	@GenericGenerator(name = "guid", strategy = "guid")
	private String id = null;
	@Column(name = "BUSSINESS_NAME", length = 28, nullable = false, insertable = true, updatable = false)
	private String bussinessName;
	@Column(name = "OPEN", nullable = false)
	private Boolean open = Boolean.valueOf(false);
	@Column(name = "IS_FLG", nullable = false)
	private Boolean isFlg = Boolean.valueOf(false);
	@Column(name = "BUSSINESS_ID", nullable = false)
	private String bussinessId;
	
	public String getId() {
		return id;
	}
	public String getBussinessName() {
		return bussinessName;
	}
	public Boolean getOpen() {
		return open;
	}
	public Boolean getIsFlg() {
		return isFlg;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public void setIsFlg(Boolean isFlg) {
		this.isFlg = isFlg;
	}
	public String getBussinessId() {
		return bussinessId;
	}
	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}
	
}
