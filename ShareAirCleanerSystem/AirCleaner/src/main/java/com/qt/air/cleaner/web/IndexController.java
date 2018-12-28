package com.qt.air.cleaner.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.config.shiro.vo.PermissionVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/")
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping("/main/index")
	public String index() {
		logger.debug("-------------index------------");
		/* Pageable pageable = new PageRequest(1, 2, Sort.Direction.DESC, "id");
		 PriceModel  priceModel = priceModelService.findById("299641BECC8543BC8EA3563E7A2468A3");
		 
		 PriceSystem priceSystem = priceModel.getPriceSystem();
		 Page<PriceModel> priceModels = priceModelService.findAll(new PageRequest(0,3,new Sort(Direction.DESC, "createTime")), "第一");
		 System.out.println(priceModels.getContent());
		System.out.println(null == userService.getUserByLoginName("admin") ? "用户不存在" : "登陆成功");*/
		return "index";
	}
	@RequestMapping("/home")
	public String toHome() {
		logger.debug("===111-------------home------------");
		return "home";
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, Map<String, Object> map) throws Exception{
		logger.info("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
		String exception = (String) request.getAttribute("shiroLoginFailure");
		logger.info("exception=" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                logger.info("UnknownAccountException -- > 账号不存在：");
                msg = "账号不存在";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                logger.info("IncorrectCredentialsException -- > 密码不正确：");
                msg = "密码不正确";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                logger.info("kaptchaValidateFailed -- > 验证码错误");
                msg = "验证码错误";
            } else {
                msg = "else >> "+exception;
                logger.info("else -- >" + exception);
            }
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理
		return "system/login";
	}

	/**
	 * 构建菜单
	 * 
	 * @return
	 */
	@RequestMapping("/nav/main")
	@ResponseBody
	public JSONObject navMainMenu() {
		 List<PermissionVo> permissions = new ArrayList<>();
		 Session session = SecurityUtils.getSubject().getSession();
		 Object permisObj = session.getAttribute(Constants.PERMISSION_SESSION);
		 permissions = ( List<PermissionVo>) permisObj;
		 JSONObject result = null;
		 JSONArray data = new JSONArray();
		 JSONArray childrenData = null;
		 if(!CollectionUtils.isEmpty(permissions)) {
			 for(PermissionVo view : permissions) {
				 if(null != view) {
					if(view != null&&StringUtils.isEmpty(view.getParentKey())) {
						result = new JSONObject();
						result.put("text", view.getName());
						result.put("icon", view.getCssClass());
						if(StringUtils.isNotBlank(view.getUrl())) {
							result.put("href", view.getUrl());
						}
						childrenData = this.findchildren(view.getId(),permissions);
						if(childrenData.size()!=0) {
							result.put("subset", childrenData);
						}
						data.add(result);
					} 
				 }
			 }
		 }
		 result = new JSONObject();
		 result.put("data", data);
		 return result;
	}
	
	/**
	 * 遍历子菜单
	 * 
	 * @param id
	 * @param permissions
	 * @return
	 */
	private JSONArray findchildren(String id,List<PermissionVo> permissions) {
		JSONArray result = new JSONArray();
		JSONObject data = null;
		for(PermissionVo permissionVo : permissions) {
			data  = new JSONObject();
			if(permissionVo!=null && StringUtils.equals(id, permissionVo.getParentKey())) {
				data.put("text", permissionVo.getName());
				data.put("icon", permissionVo.getCssClass());
				data.put("href", permissionVo.getUrl());
				result.add(data);
			}
		}
		return result;
	}
}
