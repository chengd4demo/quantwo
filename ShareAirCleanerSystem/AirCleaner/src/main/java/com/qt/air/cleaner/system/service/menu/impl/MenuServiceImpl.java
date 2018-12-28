package com.qt.air.cleaner.system.service.menu.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.system.domain.menu.Permission;
import com.qt.air.cleaner.system.repository.menu.MenuRepository;
import com.qt.air.cleaner.system.service.menu.MenuService;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.menu.MenuView;

@Service
@Transactional
public class MenuServiceImpl implements MenuService{
	@Resource
	MenuRepository menuRepository;
	@Override
	public Page<Permission> findAllMenu(MenuView menuView, Pageable pageable) {
		Specification<Permission> specification = new Specification<Permission>() {
			@Override
			public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = menuView.getName();
				if (StringUtils.isNotBlank(name)) { // 菜单名称
					Predicate p1 = cb.like(root.get("name"), "%" + name + "%");
					conditions.add(p1);
				}
				Predicate p2 = cb.equal(root.get("removed"), false);
				conditions.add(p2);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return menuRepository.findAll(specification, pageable);
	}

	@Override
	public Permission findById(String id) {
		return menuRepository.findByIdAndRemoved(id, false);
	}

	@Override
	public void saveOrUpdate(MenuView menuView) {
		if (menuView != null) {
			Permission permission = null;
			String id = menuView.getId();
			Date nowDate = Calendar.getInstance().getTime();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isNoneBlank(id)) {
				permission = menuRepository.findByIdAndRemoved(id, false);
				if (null == permission) {
					throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(),
							ErrorCodeEnum.ES_1001.getMessage());
				} else {
					menuView.setSort(permission.getSort());
					BeanUtils.copyProperties(menuView, permission);
					permission.setLastOperateTime(nowDate);
					permission.setLastOperator(principal.getUser().getUsername());
				}
			} else {
				permission =  new Permission();
				BeanUtils.copyProperties(menuView, permission);
				permission.setSort((int)(menuRepository.count()+1)	);
				permission.setCreateTime(nowDate);
				permission.setCreater(principal.getUser().getUsername());
			}
			menuRepository.save(permission);

		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
		
	}

	@Override
	public void delete(String id) {
		if (StringUtils.isNotBlank(id)) {
			Permission permission = menuRepository.findByIdAndRemoved(id, false);
			if (permission != null) {
				permission.setRemoved(true);
				menuRepository.saveAndFlush(permission);
				if (StringUtils.isEmpty(permission.getUrl())) {
					menuRepository.deleteChildrenMenu(permission.getId());
				}
			}
		}
	}

	@Override
	public List<Permission> findAllMenu() {
		return menuRepository.findByRemoved(Boolean.FALSE);
		
	}

}
