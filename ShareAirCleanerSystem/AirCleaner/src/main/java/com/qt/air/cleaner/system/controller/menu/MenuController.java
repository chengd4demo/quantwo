package com.qt.air.cleaner.system.controller.menu;

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
import com.qt.air.cleaner.system.controller.security.SecurityUserController;
import com.qt.air.cleaner.system.domain.menu.Permission;
import com.qt.air.cleaner.system.service.menu.MenuService;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;
import com.qt.air.cleaner.vo.menu.MenuView;

@Controller
@RequestMapping("system/menu")
public class MenuController {
	private final static Logger logger = LoggerFactory.getLogger(SecurityUserController.class);
	private final String SYSTEM_MENU_INDEX = "view/system/menu/queryMenuIndex",
			SYSTEM_MENU_EDIT = "view/system/menu/editMenuIndex",
			SYSTEM_MENU_ICON = "view/system/menu/menuIcon";
	@Autowired
	MenuService menuService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/icon")
	public String menuIcon() {
		return SYSTEM_MENU_ICON;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return SYSTEM_MENU_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<MenuView> page(Integer page,Integer limit,MenuView params){
		logger.debug("菜单信息分页查询请求参数对象：{}",params.toString());
		Page<Permission> permissions= menuService.findAllMenu(params, 
				new PageRequest(page-1, limit, Sort.Direction.ASC, "sort"));
		List<MenuView> result = new ArrayList<>();
		for(Permission permission:permissions.getContent()){
			if(permissions != null){
				result.add(new MenuView(permission));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, permissions.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id, String parentMenuId, Model model) {
		MenuView menuView = null;
		if (StringUtils.isNotBlank(id)) {
			Permission permission = menuService.findById(id);
			if(permission != null) {
				menuView = new MenuView(permission);
				menuView.setLegend(Constants.MENU_EDIT_LEGEND);
			}
		} else {
			menuView = new MenuView();
			if(StringUtils.isNotBlank(parentMenuId)){
				menuView.setParentKey(parentMenuId);
			}
			menuView.setLegend(Constants.MENU_ADD_LEGEND);
		}
		model.addAttribute("menu", menuView);
		model.addAttribute("menues", menuService.findAllMenu());
		return SYSTEM_MENU_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(MenuView menuView){
		logger.debug("菜单信息更新请求参数对象：{}",menuView.toString());
		try {
			menuService.saveOrUpdate(menuView);
		} catch (Exception e) {
			logger.error("菜单信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("菜单信息删除请求参数{}" + id);
		try {
			menuService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
}
