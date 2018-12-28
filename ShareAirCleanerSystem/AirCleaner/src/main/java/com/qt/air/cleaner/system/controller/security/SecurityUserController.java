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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.system.domain.security.User;
import com.qt.air.cleaner.system.service.security.RoleService;
import com.qt.air.cleaner.system.service.security.UserService;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;
import com.qt.air.cleaner.vo.security.UserView;

/**
 * 用户管理
 * @author Jansan.Ma
 *
 */
@Controller
@RequestMapping("security/user")
public class SecurityUserController {
	private final static Logger logger = LoggerFactory.getLogger(SecurityUserController.class);
	private final String SECURITY_USER_INDEX = "view/security/user/queryUserIndex",
			SECURITY_USER_EDIT = "view/security/user/editUserIndex";
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return SECURITY_USER_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<UserView> page(Integer page,Integer limit,UserView params){
		logger.debug("用户信息分页查询请求参数对象：{}",params.toString());
		Page<User> users= userService.findAllUser(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<UserView> result = new ArrayList<>();
		for(User user:users.getContent()){
			if(user != null){
				result.add(new UserView(user));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, users.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id, Model model) {
		UserView userView = null;
		if (StringUtils.isNotBlank(id)) {
			logger.debug("用户信息编辑请求参数：{}",id);
			User user = userService.findById(id);
			if(user != null) {
				userView = new UserView(user);
				userView.setLegend(Constants.USER_EDIT_LEGEND);
				userView.setRoleId(userService.findByUserRoleId(user.getId()));
			}
		} else {
			userView = new UserView();
			userView.setLegend(Constants.USER_ADD_LEGEND);
		}
		model.addAttribute("user", userView);
		model.addAttribute("roles", roleService.findAll(false));
		return SECURITY_USER_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(UserView userView){
		logger.debug("用户信息更新请求参数对象：{}",userView.toString());
		try {
			userService.saveOrUpdate(userView);
		} catch (Exception e) {
			logger.error("用户信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		} 
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("用户信息删除请求参数{}" + id);
		try {
			userService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "update/{id}")
	@ResponseBody
	public RetResult<Object>  updateStatus(@PathVariable("id") String id){
		logger.debug("用户信息启用参数{}" + id);
		try {
			userService.updateSstatus(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	
}
