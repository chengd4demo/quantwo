package com.qt.air.cleaner.system.domain.security;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.singalrain.framework.core.bo.GenericEntity;

/**
 * @Description 用户与角色关系对象
 * @author Jansan.Ma
 *
 */
@Entity
@Table(name="T_SYS_USER_ROLE")
public class UserRole  extends GenericEntity{

	 private static final long serialVersionUID = -56720255622342923L;

	    private String id;

	    /** 用户ID **/
	    private String userId;

	    /** 角色ID **/
	    private String roleId;

	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public String getRoleId() {
	        return roleId;
	    }

	    public void setRoleId(String roleId) {
	        this.roleId = roleId;
	    }

}
