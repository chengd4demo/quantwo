package com.qt.air.cleaner.system.repository.security;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.system.domain.security.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role, String> {
	List<Role> findRoleById(String id);
	Role findRoleByCode(String code);
	Page<Role> findAll(Specification<Role> specification, Pageable pageable);
	Role findByIdAndRemoved(String id, boolean removed);
	List<Role> findByRemoved(Boolean remoced);
}
