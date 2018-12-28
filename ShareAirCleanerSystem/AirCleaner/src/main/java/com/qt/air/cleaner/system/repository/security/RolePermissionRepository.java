package com.qt.air.cleaner.system.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.system.domain.security.RolePermission;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, String> {
	List<RolePermission> findByRoleId(String roleId);
}
