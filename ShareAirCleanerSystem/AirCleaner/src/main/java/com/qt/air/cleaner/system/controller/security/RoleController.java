package com.qt.air.cleaner.system.controller.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.system.domain.security.Role;
import com.qt.air.cleaner.system.domain.security.RolePermission;
import com.qt.air.cleaner.system.service.security.PermissionService;
import com.qt.air.cleaner.system.service.security.RolePermissionService;
import com.qt.air.cleaner.system.service.security.RoleService;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;
import com.qt.air.cleaner.vo.security.RoleView;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("security/role")
public class RoleController {
	private final static Logger logger = LoggerFactory.getLogger(SecurityUserController.class);
	private final String SECURITY_ROLE_INDEX = "view/security/role/queryRoleIndex",
			SECURITY_ROLE_EDIT = "view/security/role/editRoleIndex";
	@Autowired
	RoleService roleService;
	@Autowired
	PermissionService permissionService;
	@Autowired
	RolePermissionService rolePermissionService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return SECURITY_ROLE_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<RoleView> page(Integer page,Integer limit,RoleView params){
		logger.debug("角色信息分页查询请求参数对象：{}",params.toString());
		Page<Role> roles= roleService.findAllRole(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<RoleView> result = new ArrayList<>();
		for(Role role:roles.getContent()){
			if(role != null){
				result.add(new RoleView(role));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, roles.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id, Model model) {
		RoleView roleView = null;
		if (StringUtils.isNotBlank(id)) {
			Role role = roleService.findById(id);
			if(role != null) {
				roleView = new RoleView(role);
				roleView.setLegend(Constants.ROLE_EDIT_LEGEND);
			}
		} else {
			roleView = new RoleView();
			roleView.setLegend(Constants.ROLE_ADD_LEGEND);
		}
		model.addAttribute("role", roleView);
		return SECURITY_ROLE_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(RoleView roleView){
		logger.debug("角色信息更新请求参数对象：{}",roleView.toString());
		try {
			roleService.saveOrUpdate(roleView);
		} catch (Exception e) {
			logger.error("角色信息更新错误异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("角色信息删除请求参数{}" + id);
		try {
			roleService.delete(id);
		} catch (Exception e) {
			logger.error("角色信息删除错误异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/menue")
	@ResponseBody
	public JSONObject queryMenues(String roleId) {
		String result = permissionService.getByRoleIdPermissions(roleId);
		return JSONObject.fromObject(result);
	}
	
	@RequestMapping(method = RequestMethod.POST,path = "/authorization")
	@ResponseBody
	public RetResult<Object> roleAuthorization(@RequestBody List<RolePermission> params,String roleId) {
		logger.debug("角色授权请求参数{}" + roleId);
		try {
			 if(StringUtils.isEmpty(roleId)) {
				 return RetResponse.makeErrRsp(ErrorCodeEnum.ES_1001.getMessage());
			 }
			 boolean isOk = rolePermissionService.saveRolePerission(params,roleId);
			 if(!isOk) {
				 return RetResponse.makeErrRsp(ErrorCodeEnum.ES_1001.getMessage());
			 }
		} catch (Exception e) {
			logger.error("角色授权错误异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_1001.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
}
