package com.qt.air.cleaner.system.service.security;

import java.util.List;

import com.qt.air.cleaner.system.domain.security.RolePermission;

public interface RolePermissionService {

	/**
	 * 角色授权
	 * @param params
	 * @param roleId
	 * @return
	 */
	public boolean saveRolePerission(List<RolePermission> params, String roleId);

}
