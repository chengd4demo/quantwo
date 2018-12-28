package com.qt.air.cleaner.system.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qt.air.cleaner.system.domain.security.UserRole;

public interface UserRoleRepository  extends JpaRepository<UserRole, String> {
	List<UserRole> findByUserId(String userId);
	
}
