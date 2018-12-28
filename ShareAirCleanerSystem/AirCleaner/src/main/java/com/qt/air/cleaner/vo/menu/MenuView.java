package com.qt.air.cleaner.vo.menu;

import java.io.Serializable;

import com.qt.air.cleaner.system.domain.menu.Permission;

public class MenuView implements Serializable {
	private static final long serialVersionUID = 741702695780984355L;
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
    
    private String legend;
    
    public MenuView(Permission permission) {
		super();
		this.setId(permission.getId());
		this.setName(permission.getName());
		this.setCssClass(permission.getCssClass());
		this.setKey(permission.getKey());
		this.setHide(permission.getHide());
		this.setLev(permission.getLev());
		this.setUrl(permission.getUrl());
		this.setSort(permission.getSort());
		this.setParentKey(permission.getParentKey());
	}

	public MenuView() {
		super();
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

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	@Override
	public String toString() {
		return "MenuView [id=" + id + ", name=" + name + ", cssClass=" + cssClass + ", key=" + key + ", hide=" + hide
				+ ", lev=" + lev + ", url=" + url + ", sort=" + sort + ", parentKey=" + parentKey + ", legend=" + legend
				+ "]";
	}
    
}
