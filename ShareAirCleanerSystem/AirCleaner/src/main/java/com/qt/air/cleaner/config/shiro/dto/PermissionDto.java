package com.qt.air.cleaner.config.shiro.dto;

import java.io.Serializable;

public class PermissionDto implements Serializable {
	private static final long serialVersionUID = 4952212708267025441L;
	private String id;

	/** 菜单名称 **/
	private String name;

	/** 菜单样式图标名称 **/
	private String cssClass;

	/** 菜单编码 **/
	private String key;

	/** 菜单是否显示 **/
	private Integer hide;

	/** 菜单级别，最多三级 **/
	private Integer lev;

	/** URL **/
	private String url;

	/** 显示顺序 **/
	private Integer sort;

	/** 父菜单编码 **/
	private String parentKey;

	public PermissionDto() {

	}

	public PermissionDto(String id, String name, String cssClass, String key, Integer hide, Integer lev, String url,
			Integer sort, String parentKey) {
		super();
		this.id = id;
		this.name = name;
		this.cssClass = cssClass;
		this.key = key;
		this.hide = hide;
		this.lev = lev;
		this.url = url;
		this.sort = sort;
		this.parentKey = parentKey;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCssClass() {
		return cssClass;
	}

	public String getKey() {
		return key;
	}

	public Integer getHide() {
		return hide;
	}

	public Integer getLev() {
		return lev;
	}

	public String getUrl() {
		return url;
	}

	public Integer getSort() {
		return sort;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setHide(Integer hide) {
		this.hide = hide;
	}

	public void setLev(Integer lev) {
		this.lev = lev;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

}
