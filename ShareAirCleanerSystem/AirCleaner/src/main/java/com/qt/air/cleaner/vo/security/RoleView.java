package com.qt.air.cleaner.vo.security;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.system.domain.security.Role;

public class RoleView implements Serializable {
	private static final long serialVersionUID = -3822901882859754829L;
	private String id;	//主键id
	private String name;	//角色名称
	private String remark; //备注
	private String createTime; //创建时间
	private String creater;	//创建用户
	private String legend;
	@JsonIgnore
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public RoleView() {
		super();
	}
	public RoleView(Role role) {
		super();
		this.setId(role.getId());
		this.setName(role.getName());
		this.setCreater(role.getCreater());
		this.setRemark(role.getRemark());
		this.setCreateTime(dateFormat.format(role.getCreateTime()));
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getRemark() {
		return remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public String toString() {
		return "RoleView [id=" + id + ", name=" + name + ", remark=" + remark + ", createTime=" + createTime
				+ ", creater=" + creater + ", dateFormat=" + dateFormat + "]";
	}
}
