package com.qt.air.cleaner.system.service.security.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qt.air.cleaner.system.domain.security.RolePermission;
import com.qt.air.cleaner.system.repository.security.RolePermissionRepository;
import com.qt.air.cleaner.system.service.security.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService{
	@Resource
	RolePermissionRepository rolePermissionRepository;
	
	@Override
	public boolean saveRolePerission(List<RolePermission> params, String roleId) {
		RolePermission rolePermission = new RolePermission();
		rolePermission.setId(roleId);
		rolePermissionRepository.delete(rolePermission);
		List<RolePermission> result  = rolePermissionRepository.save(params);
		if(CollectionUtils.isEmpty(result)) {
			return false;
		}
		return true;
	}
}
