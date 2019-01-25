package com.qt.air.cleaner.base.domain;

import com.qt.air.cleaner.base.domain.Removable;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class GenericEntity implements Serializable, Removable, Cloneable {
	private static final long serialVersionUID = 2673192457225120085L;
	@Id
	@GeneratedValue(generator = "guid")
	@GenericGenerator(name = "guid", strategy = "guid")
	private String id = null;
	@Column(name = "REMOVED", nullable = false)
	@Type(type = "yes_no")
	private Boolean removed = Boolean.valueOf(false);
	@Column(name = "CREATER", length = 40, nullable = false, insertable = true, updatable = true)
	private String creater;
	@Column(name = "CREATE_TIME", nullable = false, insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createTime;
	@Column(name = "OPERATOR", length = 40, insertable = false, updatable = true)
	private String lastOperator;
	@Column(name = "OPERATE_TIME", insertable = false, updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date lastOperateTime;

	public String getId() {
		return this.id;// 110
	}

	public void setId(String id) {
		this.id = id;// 118
	}// 119

	public Boolean getRemoved() {
		return this.removed;// 126
	}

	public Boolean isEntityRemoved() {
		return this.getRemoved();// 131
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;// 139
	}// 140

	public String getCreater() {
		return this.creater;// 147
	}

	public void setCreater(String creater) {
		this.creater = creater;// 155
	}// 156

	public Date getCreateTime() {
		return this.createTime;// 163
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;// 171
	}// 172

	public String getLastOperator() {
		return this.lastOperator;// 179
	}

	public void setLastOperator(String lastOperator) {
		this.lastOperator = lastOperator;// 187
	}// 188

	public Date getLastOperateTime() {
		return this.lastOperateTime;// 195
	}

	public void setLastOperateTime(Date lastOperateTime) {
		this.lastOperateTime = lastOperateTime;// 203
	}// 204

	public int hashCode() {
		boolean prime = true;// 209
		byte result = 1;// 210
		int result1 = 31 * result + (this.id == null ? 0 : this.id.hashCode());// 211
		return result1;// 212
	}

	public boolean equals(Object obj) {
		if (this == obj) {// 218
			return true;
		} else if (obj == null) {// 219
			return false;
		} else if (!(obj instanceof GenericEntity)) {// 220
			return false;
		} else {
			GenericEntity other = (GenericEntity) obj;// 221
			if (this.id == null) {// 222
				if (other.id != null) {// 223
					return false;
				}
			} else if (!this.id.equals(other.id)) {// 224
				return false;
			}

			return true;// 225
		}
	}
}