package com.qt.air.cleaner.market.controller.platform;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.service.platform.PlatformSetService;
import com.qt.air.cleaner.market.vo.platform.PlatformSetView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("market/platform")
public class PlatformSetController {
	private final Logger logger = LoggerFactory.getLogger(PlatformSetController.class);
	private final String PLATFORM_SHAREPROFIT_INDEX = "view/market/platform/queryNewShareProfile",
						 PLATFORM_SHAREPROFIT_EDIT = "view/market/platform/editNewShareProfile";
	@Autowired
	PlatformSetService platformSetService; 
	
	/**
	 * 分润信息
	 * 
	 * @param referrer
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index(String referrer,Model model) {
		model.addAttribute("deviceDit", referrer);
		model.addAttribute("pids", platformSetService.findAll());
		return PLATFORM_SHAREPROFIT_INDEX;
	}
	
	/**
	 * 分润查询分页查询
	 * 
	 * @param page
	 * @param limit
	 * @param platformSetView
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/query")
	@ResponseBody
	public JSONObject page(Integer page,Integer limit,@RequestBody PlatformSetView platformSetView){
		logger.debug("平台设定插叙求参数对象：{}",platformSetView.toString());
		JSONArray resultJSONArray = new JSONArray();
		JSONObject result = new JSONObject();
		try {
			long tatal = platformSetService.findCount(platformSetView);
			Page<ShareProfit> resultPage = platformSetService.findAllSet(platformSetView, new PageRequest(page-1, limit));
			if(result != null) {
				List<ShareProfit> content = resultPage.getContent();
				if(!CollectionUtils.isEmpty(resultPage.getContent())) {
					JSONObject obj = new JSONObject();
					for(ShareProfit profit : content) {
						obj.put("id", profit.getId());
						if (StringUtils.isEmpty(platformSetView.getType()) && StringUtils.isEmpty(platformSetView.getPid())) {
							obj.put("pid", profit.getPid());
						} else {
							obj.put("pid", "0");
						}
						
						if(StringUtils.equals("0", profit.getPid())) {
							obj.put("title","成都公司");
							obj.put("name",  profit.getName());
						} else {
							obj.put("title",profit.getName());
							obj.put("name","客户");
						}
						obj.put("type", profit.getType());
						obj.put("scale", profit.getScale());
						resultJSONArray.add(obj);
					}
				}
				result.put("data", resultJSONArray);
				result.put("pageCount", tatal);
			}
			
		} catch (Exception e) {
			logger.error("投资商信息更新报错异常:{}", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 分润信息查询
	 * 
	 * @param id
	 * @param level
	 * @param model
	 * @return
	 */
	@RequestMapping (method = RequestMethod.GET,path = "/edit")
	public String edit(String id,String level,String pid, Model model) {
		PlatformSetView platform = null;
		if (StringUtils.isNotBlank(id)) {
			ShareProfit shareProfit = platformSetService.findById(id);
			if(shareProfit != null) {
				platform = new PlatformSetView(shareProfit);
				platform.setLegend(Constants.SCALE_EDIT_LEGEND);
			}
		} else {
			platform = new PlatformSetView();
			if(StringUtils.isNotEmpty(level)) {
				platform.setType(ShareProfit.ACCOUNT_TYPE_COMPANY);
				platform.setTypeDisabled(true);
			}
			if(StringUtils.isNotEmpty(pid)) {
				platform.setPid(pid);
				platform.setPidDisabled(true);
			}
			platform.setLegend(Constants.SCALE_ADD_LEGEND);
		}
		model.addAttribute("platform", platform);
		//编辑页面所属上级下拉列表
		model.addAttribute("pids", platformSetService.findAll());
		return PLATFORM_SHAREPROFIT_EDIT;
	}
	
	/**
	 * 分润信息编辑
	 * @param platformSetView
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, path = "edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(PlatformSetView platformSetView){
		logger.debug("分润信息更新请求参数对象：{}",platformSetView.toString());
		try {
			platformSetService.saveOrUpdate(platformSetView);
		} catch (Exception e) {
			logger.error("分润信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_0000.getMessage());
		}
		return RetResponse.makeOKRsp();
		
	}
	
	/**
	 * 分润信息删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("分润信息删除请求参数{}" + id);
		boolean isOk = false;
		try {
			isOk = platformSetService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(e.getMessage());
		}
		if(isOk) {
			return RetResponse.makeOKRsp();
		} else {
			return RetResponse.makeErrRsp("该比例已被绑定使用，无法删除,若确定需要删除，请先将所有绑定该比例的设备取消。");
		}
		
	}
}
