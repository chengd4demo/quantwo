package com.qt.air.cleaner.system.domain.menu;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name="T_SYS_PERMISSION")
public class Permission  extends GenericEntity{
	private static final long serialVersionUID = -7141829387338999544L;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }
    
    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }
}
