package com.qt.air.cleaner.system.service.security.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.system.domain.security.Role;
import com.qt.air.cleaner.system.repository.security.RoleRepository;
import com.qt.air.cleaner.system.service.security.RoleService;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.security.RoleView;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired RoleRepository roleRepository;
	@Override
	public List<Role> findRoleByUserId(String id) {
		return roleRepository.findRoleById(id);
	}
	@Override
	public void addRole(Role role) {
		roleRepository.save(role);
	}
	@Override
	public Role findRoleByCode(String code) {
		return roleRepository.findRoleByCode(code);
	}
	@Override
	public void addRolePermission(String roleCode, String permissionKey) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 角色信息分页查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Role> findAllRole(RoleView params, Pageable pageable) {
		Specification<Role> specification = new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String name = params.getName();
				if (StringUtils.isNotBlank(name)) { // 角色名称
					Predicate p1 = cb.like(root.get("name"), "%" + name + "%");
					conditions.add(p1);
				}
				Predicate p9 = cb.equal(root.get("removed"), false);
				conditions.add(p9);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return roleRepository.findAll(specification, pageable);
	}
	
	/**
	 * 角色信息新增或更新
	 * 
	 * @param roleView
	 */
	@Override
	public void saveOrUpdate(RoleView roleView) {
		if (roleView != null) {
			Role role = null;
			String id = roleView.getId();
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			Date nowDate = Calendar.getInstance().getTime();
			if (StringUtils.isNotBlank(id)) {
				role = roleRepository.findOne(id);
				BeanUtils.copyProperties(roleView, role);
				role.setLastOperateTime(nowDate);
				role.setLastOperator(principal.getUser().getUsername());
			} else {
				role = new Role();
				BeanUtils.copyProperties(roleView, role);
				role.setCreateTime(nowDate);
				role.setCreater(principal.getUser().getUsername());
			}
			roleRepository.saveAndFlush(role);
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
	}
	
	/**
	 * 角色信息删除
	 * 
	 * @param id
	 */
	@Override
	public void delete(String id) {
		Role role = roleRepository.findOne(id);
		role.setRemoved(Boolean.TRUE);
		roleRepository.saveAndFlush(role);
	}
	
	/**
	 * 角色信息查询
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Role findById(String id) {
		return roleRepository.findByIdAndRemoved(id,false);
	}
	
	
	/**
	 * 查询角色列表
	 * 
	 * @return
	 */
	public List<Role> findAll(boolean removed){
		return roleRepository.findByRemoved(removed);
	}
	
	/**
	 * 角色授权
	 * 
	 * @param roleId
	 * @return
	 */
	@Override
	public boolean roleAuthorization(String roleId) {
		return false;
	}
}
