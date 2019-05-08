package com.qt.air.cleaner.device.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.UpdateTimestamp;

import com.qt.air.cleaner.base.domain.GenericEntity;



@Entity
@Table(name = "MK_DEVICE_CHIP", indexes = {
        @Index(columnList = "MACH_NO", name = "DEVICEMACHNO")
})
public class DeviceChip  extends GenericEntity {
	private static final long serialVersionUID = 3715875283876146844L;
	@Version
	private Integer version = 0;
	@Column(name = "MACH_NO", nullable = false, length = 255, unique = true, insertable = true, updatable = false)
	private String machNo;
	@Column(name = "PM_VALUE")
	private Integer pmValue = 0;
	@Column(name = "CHANGE_TIME", insertable = false, updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date changeTime;
	
	public Integer getVersion() {
		return version;
	}
	public String getMachNo() {
		return machNo;
	}
	public Integer getPmValue() {
		return pmValue;
	}
	public void setPmValue(Integer pmValue) {
		this.pmValue = pmValue;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public void setMachNo(String machNo) {
		this.machNo = machNo;
	}
	public Date getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
}
