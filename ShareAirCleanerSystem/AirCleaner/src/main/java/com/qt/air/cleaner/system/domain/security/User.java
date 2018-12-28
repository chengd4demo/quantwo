package com.qt.air.cleaner.system.domain.security;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name="T_SYS_USER")
public class User  extends GenericEntity {
	private static final long serialVersionUID = 1227495748732942139L;

	/** email **/
	private String email;

	/** 用户名 **/
	private String username;

	/** 真实名称 **/
	private String trueName;

	/** 密码 **/
	private String password;

	/** salt码 **/
	private String salt;

	/** 状态 **/
	private Integer status;

	/** 工作流审批的组织ID **/
	private String organizeId;

	/** 创建时间 **/
	private Date createTime;

	/** 最后登录时间 **/
	private Date lastLoginTime;

	/** 用户最后修改时间 **/
	private Date modifyTime;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeID) {
		this.organizeId = organizeID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
