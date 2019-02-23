package com.qt.air.cleaner.market.controller.platform;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.qt.air.cleaner.market.domain.platform.ShareProfit;
import com.qt.air.cleaner.market.service.platform.PlatformSetService;
import com.qt.air.cleaner.market.vo.platform.PlatformSetView;

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
	
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return PLATFORM_SHAREPROFIT_INDEX;
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/edit")
	public String edit() {
		
		return PLATFORM_SHAREPROFIT_EDIT;
	}
	
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
					String type = null;
					for(ShareProfit profit : content) {
						obj.put("id", profit.getId());
						obj.put("pid", profit.getPid());
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
}
