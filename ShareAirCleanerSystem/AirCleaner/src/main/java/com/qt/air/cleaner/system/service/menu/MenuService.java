package com.qt.air.cleaner.system.service.menu;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.system.domain.menu.Permission;
import com.qt.air.cleaner.vo.menu.MenuView;

public interface MenuService {
	Page<Permission> findAllMenu(MenuView params, Pageable pageable);

	Permission findById(String id);

	void saveOrUpdate(MenuView menuView);

	void delete(String id);

	List<Permission> findAllMenu();
	
}
