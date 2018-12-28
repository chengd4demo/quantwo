package com.qt.air.cleaner.system.repository.security;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.system.domain.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	User findByUsernameAndRemoved(String username,boolean removed);
	User findByUsernameAndRemovedAndStatus(String username,boolean removed,Integer status);
	Page<User> findAll(Specification<User> specification, Pageable pageable);
	User findByIdAndRemoved(String id,boolean removed);
}
