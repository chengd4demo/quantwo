package com.qt.air.cleaner.config.shiro.vo;

import java.util.ArrayList;
import java.util.List;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.system.domain.menu.Permission;

public enum PermissionEnumUtil {
	管理首页("管理首页", "fa-home", "glsy", null, "index", 1, 1), 

    基本信息维护("基本信息维护", "", "jbxxwh", null, null, 1, 10),
    公司信息维护("公司信息维护", "", "gsxxwh", "znxy", "news/add", 2, 11),
    商家信息维护("商家信息维护", "", "sjxxwh", "znxy", "news/list",2, 12),
    促销员信息维护("促销员信息维护", "", "cxyxxwh", "znxy", "news/list", 2, 13),
    
    系统配置("系统配置", "fa-gear", "xtpz", null, "view/sysconfig/setconfig" , 1, 20),
    菜单管理("菜单管理", "fa-desktop", "dxgl", null, "view/tenant/tenant-list" , 1, 30),
    权限管理("电商配置", "fa-gears", "dspz", null, "view/tenantConfig/list" , 1, 40),
    用户管理("地区城市管理", "fa-map-marker", "dccsgl", null, null , 1, 50),
    ;

    private PermissionEnumUtil(String name, String cssClass, String key, String parentKey, String url, Integer lev, Integer sort) {
        this.name = name;
        this.cssClass = cssClass;
        this.key = key;
        this.lev=lev;
        this.sort = sort;
        this.url = url;
        this.hide = Constants.STATUS_VALID;
        this.parentKey = parentKey;
    }
    
    public static List<Permission> getPermissions(){
        List<Permission> list = new ArrayList<>();
        PermissionEnumUtil[] pers = PermissionEnumUtil.values();
        Permission per = null;
        for (PermissionEnumUtil p : pers) {
            per = new Permission();
            per.setName(p.getName());
            per.setCssClass(p.getCssClass());
            per.setKey(p.getKey());
            per.setHide(p.getHide());
            per.setUrl(p.getUrl());
            per.setLev(p.getLev());
            per.setSort(p.getSort());
            per.setParentKey(p.getParentKey());
            list.add(per);
        }
        return list;
    }

    private String name;
    private String cssClass;
    private String key;
    private Integer hide;
    private String url;
    private Integer lev;
    private Integer sort;
    private String parentKey;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
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
