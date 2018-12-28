package com.qt.air.cleaner.system.service.security;

import java.util.List;

import com.qt.air.cleaner.config.shiro.vo.PermissionVo;
import com.qt.air.cleaner.system.domain.menu.Permission;

public interface PermissionService {
	/**
     * 查询用户所能访问的所有菜单
     *
     * @param userId
     *            用户ID
     * @return permissions 菜单
     */
    public List<PermissionVo> getPermissions(String userId);

    /**
     * 添加 菜单
     *
     * @param permission
     *            菜单项
     */
    public void addPermission(Permission permission);
    
    /**
     * 根据角色查询菜单
     * @param roleId
     * @return
     */
    public String getByRoleIdPermissions(String roleId);
    
}
