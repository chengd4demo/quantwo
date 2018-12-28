package com.qt.air.cleaner.system.domain.security;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name="T_SYS_ROLE")
public class Role extends GenericEntity{
	 private static final long serialVersionUID = -6982490361440305761L;
	    /** 角色名称 **/
	    private String name;

	    /** 编码 **/
	    private String code;

	    /** 备注 **/
	    private String remark;

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getCode() {
	        return code;
	    }

	    public void setCode(String code) {
	        this.code = code;
	    }

	    public String getRemark() {
	        return remark;
	    }

	    public void setRemark(String remark) {
	        this.remark = remark;
	    }
}
