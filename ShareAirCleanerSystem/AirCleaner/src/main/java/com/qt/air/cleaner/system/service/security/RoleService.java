package com.qt.air.cleaner.system.service.security;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.system.domain.security.Role;
import com.qt.air.cleaner.vo.security.RoleView;

/**
 * @author Administrator
 *
 */
public interface RoleService {
	/**
     * 添加一个角色 ，若已经存在同名角色，则不创建
     *
     * @param role
     *            角色对象
     */
    public void addRole(Role role);

    /**
     * 根据编码查询角色
     *
     * @param code
     *            角色编码
     * @return
     */
    public Role findRoleByCode(String code);

    /**
     * 根据用户查询对应所有角色
     *
     * @param userId
     *            用户Id
     * @return roles 
     */
    public List<Role> findRoleByUserId(String userId);

    /**
     * 给角色授权
     *
     * @param roleCode
     *            角色编码
     * @param permissionKey
     *            授权对应的KEY
     * @return
     */
    public void addRolePermission(String roleCode, String permissionKey);

	/**
	 * 角色信息分页查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<Role> findAllRole(RoleView params, Pageable pageable);

	/**
	 * 角色信息新增或更新
	 * 
	 * @param roleView
	 * @return
	 */
	public void saveOrUpdate(RoleView roleView);

	/**
	 * 角色信息删除
	 * 
	 * @param id
	 * @return
	 */
	public void delete(String id);

	
	/**
	 * 角色信息查询
	 * 
	 * @param id
	 * @return
	 */
	public Role findById(String id);
	
	/**
	 * 查询角色列表
	 * 
	 * @param removed
	 * @return
	 */
	public List<Role> findAll(boolean removed);

	/**
	 * 角色授权
	 * @param roleId
	 * 
	 * */
	public boolean roleAuthorization(String roleId);
	
	

}
