package com.qt.air.cleaner.system.repository.menu;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.config.shiro.dto.PermissionDto;
import com.qt.air.cleaner.system.domain.menu.Permission;

@Repository
public interface MenuRepository extends JpaRepository<Permission, String> {
	Page<Permission> findAll(Specification<Permission> specification, Pageable pageable);
	Permission findByIdAndRemoved(String id, boolean removed);
	List<Permission> findByRemoved(Boolean false1);
	List<Permission> findByParentKey(String parentKey);
	@Modifying
	@Query("update Permission p set p.removed = true where p.parentKey = :parentKey")
	void deleteChildrenMenu(@Param("parentKey") String parentKey);
	@Query("select new com.qt.air.cleaner.config.shiro.dto.PermissionDto(M.id,M.name,M.cssClass,M.key,M.hide,M.lev,M.url,M.sort,M.parentKey) from Permission M where M.id in((select RP.permissionId from RolePermission RP,UserRole UR where UR.roleId=RP.roleId AND UR.userId = :userId)) ORDER BY M.sort")
	List<PermissionDto> findPermissionsByUserId(@Param("userId") String userId);
}
