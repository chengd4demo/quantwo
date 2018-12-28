package com.qt.air.cleaner.vo.security;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.system.domain.security.User;

public class UserView implements Serializable {
	private static final long serialVersionUID = 1441951582096258137L;
	private String id;
	private String username;
	private String trueName;
	private Integer status;
	private String createTime;
	private String lastLoginTime;
	private String legend;
	private String roleId;
	private String passward;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public UserView() {
		super();
	}
	public UserView(User user) {
		super();
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setTrueName(user.getTrueName());
		this.setStatus(user.getStatus());
		this.setCreateTime(dateFormat.format(user.getCreateTime()));
		this.setLastLoginTime(user.getLastLoginTime() == null ? "-" : dateFormat.format(user.getLastLoginTime()));
	}
	public String getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getTrueName() {
		return trueName;
	}
	public Integer getStatus() {
		return status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	@Override
	public String toString() {
		return "UserView [id=" + id + ", username=" + username + ", trueName=" + trueName + ", status=" + status
				+ ", createTime=" + createTime + ", lastLoginTime=" + lastLoginTime + ", dateFormat=" + dateFormat
				+ "]";
	}
	
}
