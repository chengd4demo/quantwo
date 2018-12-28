package com.qt.air.cleaner.market.controller.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qt.air.cleaner.market.domain.account.WeiXinNotity;
import com.qt.air.cleaner.market.service.account.WeiXinNotityService;
import com.qt.air.cleaner.market.vo.account.WeiXinNotityView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/weiXinNotity")
public class WeiXinNotityController {

	private final Logger logger = LoggerFactory.getLogger(WeiXinNotityController.class);
	private final String GENERIC_WEIXINNOTITY_INDEX = "view/market/account/queryWeiXinNotityIndex";
	@Autowired
	WeiXinNotityService weiXinNotityService;

	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_WEIXINNOTITY_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<WeiXinNotityView> page(Integer page,Integer limit,WeiXinNotityView weiXinNotityView){
		logger.debug("微信消费记录分页查询请求参数对象：{}",weiXinNotityView.toString());
		Page<WeiXinNotity> weiXinNotitys= weiXinNotityService.findAllWeiXinNotity(weiXinNotityView, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<WeiXinNotityView> result = new ArrayList<>();
		for(WeiXinNotity weiXinNotity:weiXinNotitys.getContent()){
			if(weiXinNotity != null){
				result.add(new WeiXinNotityView(weiXinNotity));
			}
		}
		return new PageInfo<>(0, "数据读取成功", result, weiXinNotitys.getTotalElements());
	}
			
		
}
