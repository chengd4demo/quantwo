package com.qt.air.cleaner.system.service.security.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qt.air.cleaner.config.shiro.dto.PermissionDto;
import com.qt.air.cleaner.config.shiro.vo.PermissionVo;
import com.qt.air.cleaner.system.domain.menu.Permission;
import com.qt.air.cleaner.system.domain.security.RolePermission;
import com.qt.air.cleaner.system.repository.menu.MenuRepository;
import com.qt.air.cleaner.system.repository.security.RolePermissionRepository;
import com.qt.air.cleaner.system.service.security.PermissionService;
import com.qt.air.cleaner.vo.menu.Node;
import com.qt.air.cleaner.vo.menu.Tree;

@Service
public class PermissionServiceImpl implements PermissionService {
	
	@Resource
	MenuRepository menuRepository;
	
	@Resource
	RolePermissionRepository rolePermissionRepository;
	
	private PermissionVo convertToVo(PermissionDto per) {
		PermissionVo pvo = new PermissionVo();
		pvo.setId(per.getId());
		pvo.setName(per.getName());
		pvo.setCssClass(per.getCssClass());
		pvo.setUrl(per.getUrl());
		pvo.setKey(per.getKey());
		pvo.setParentKey(per.getParentKey());
		pvo.setHide(per.getHide());
		pvo.setLev(per.getLev());
		pvo.setSort(per.getSort());
		return pvo;
	}
	
	@Override
    public List<PermissionVo> getPermissions(String userId) {
        List<PermissionDto> permissions = menuRepository.findPermissionsByUserId(userId);

        if (CollectionUtils.isEmpty(permissions)) {
            return null;
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(permissions)) {

            Map<String, PermissionVo> oneMap = new LinkedHashMap<String, PermissionVo>();// 一级菜单
            Map<String, PermissionVo> twoMap = new LinkedHashMap<String, PermissionVo>();// 二级菜单
            Map<String, PermissionVo> threeMap = new LinkedHashMap<String, PermissionVo>();// 三级菜单

            String parentKey = null, key = null;
            Integer lev = null;
            PermissionVo child = null, parent = null, permissionVo = null;
            List<PermissionVo> permissionVos = new ArrayList<PermissionVo>();
            for (PermissionDto p : permissions) {
                key = p.getKey();
                lev = p.getLev();
                permissionVo = convertToVo(p);
             /*   if (1 == lev) {// 判断是不是模块
                    oneMap.put(key, permissionVo);
                }
                if (2 == lev) {// 判断是不是菜单分类
                    twoMap.put(key, permissionVo);
                }
                if (3 == lev) {// 判断是不是菜单
                    threeMap.put(key, permissionVo);
                }*/
                permissionVos.add(permissionVo);
            }

            List<PermissionVo> vos = null;

            // 迭代所有3级菜单， 把3级菜单挂在2级菜单分类下面去
           /* for (Entry<String, PermissionVo> vo : threeMap.entrySet()) {
                child = vo.getValue();// 3级菜单
                parentKey = child.getParentKey();// 获取3级菜单对应的2级菜单KEY，即父节点KEY
                if (twoMap.containsKey(parentKey)) {// 校验当前拿到的2级菜单KEY在twoMap集合中有没有
                    parent = twoMap.get(parentKey);// 获取对应的2级菜单

                    vos = parent.getChildren();// 获取2级菜单下3级菜单集合
                    if (CollectionUtils.isEmpty(vos)) {
                        vos = new ArrayList<>();
                    }
                    vos.add(child);// 将3级菜单挂在2级菜单下去
                    parent.setChildren(vos);
                    twoMap.put(parentKey, parent);
                }
            }

            // 迭代所有2级菜单， 把2级菜单挂在1级菜单分类下面去
            for (Entry<String, PermissionVo> vo : twoMap.entrySet()) {
                child = vo.getValue();// 2级菜单
                parentKey = child.getParentKey();// 获取2级菜单对应的1级菜单KEY，即父节点KEY
                if (oneMap.containsKey(parentKey)) {// 校验当前拿到的1级菜单KEY在oneMap集合中有没有
                    parent = oneMap.get(parentKey);// 获取对应的1级菜单

                    vos = parent.getChildren();// 获取1级菜单下2级菜单集合
                    if (CollectionUtils.isEmpty(vos)) {
                        vos = new ArrayList<PermissionVo>();
                    }
                    vos.add(child);// 将2级菜单挂在1级菜单下去
                    parent.setChildren(vos);
                    oneMap.put(parentKey, parent);
                }
            }*/

           
           
           // permissionVos.addAll(oneMap.values());
            return permissionVos;
        } else {
            return null;
        }
    }
	

	@Override
	public void addPermission(Permission permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getByRoleIdPermissions(String roleId) {
		List<Permission> menuList = new ArrayList<>();
		List<Node> nodes = new ArrayList<Node>();
		StringBuffer result = new StringBuffer();
		menuList = menuRepository.findByRemoved(false);
		if (CollectionUtils.isEmpty(menuList)) {
            return null;
        } else {
        	Node node = null;
        	Permission permission = null;
        	for(int i=0;i<menuList.size();i++) {
        		node = new Node();
        		permission = menuList.get(i);
        		if(permission!= null) {
        			node.setId(permission.getId());
        			node.setName(permission.getName());
        			node.setParentId(permission.getParentKey());
        			node.setUrl(permission.getUrl());
        		}
        		nodes.add(node);
        	}
        	List<RolePermission> rolePermissionList = new ArrayList<>();
        	if(StringUtils.isNotBlank(roleId)) {
        		rolePermissionList = rolePermissionRepository.findByRoleId(roleId);
        	}
        	Tree tree = new Tree(nodes);
        	List<Node> newNodes = new ArrayList<>(nodes.size());
        	if(!CollectionUtils.isEmpty(rolePermissionList) && !CollectionUtils.isEmpty(nodes)){
        		RolePermission rolePermission = null;
        		for(int i=0;i<rolePermissionList.size();i++){
        			rolePermission = rolePermissionList.get(i);
        			String menueId = rolePermission.getPermissionId();
        			nodes.forEach(n -> {
        				if(StringUtils.equals(n.getId(), menueId)) {
        					n.setChecked(true);
        					newNodes.add(n);
        				}
        			});
        		}
        		tree = new Tree(nodes);
        	}
        	if(nodes.size() == 0) {
        		result.append("{\"Code\":" + 1);
        	} else {
        		result.append("{\"Code\":" + 0);
        	}
        	result.append(",\"Data\":" + tree.buildTree()+"}");
        	System.out.println(result.toString());
        }
		
        return result.toString();
        
		/*List<RolePermission> rolePermissionList = new ArrayList<>();
		if(StringUtils.isNotBlank(roleId)) {
			rolePermissionList = rolePermissionRepository.findByRoleId(roleId);
		}*/
	}
	

}
