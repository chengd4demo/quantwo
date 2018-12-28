package com.qt.air.cleaner.market.controller.activity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.account.WinningConfing;
import com.qt.air.cleaner.market.service.activity.WinningConfingService;
import com.qt.air.cleaner.market.vo.activity.WinningConfingView;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/queryWinning")
public class WinningController {
	
	private final Logger logger = LoggerFactory.getLogger(WinningController.class);
	private final String ACTIVITY_QUERYWINNING_INDEX = "view/market/activity/queryWinningIndex",
						 ACTIVITY_EDITWINNING_INDEX ="view/market/activity/editWinningIndex";
	
	@Autowired
	WinningConfingService winningConfingService;
	
	@RequestMapping(method=RequestMethod.GET,path="/index")
	public String index() {
		return ACTIVITY_QUERYWINNING_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET,path ="/page")
	@ResponseBody
	public PageInfo<WinningConfingView> Page(Integer page,Integer limit,WinningConfingView params){
		logger.debug("中奖名单分页查询请求参数对象：{}",params.toString());
		Page<WinningConfing> winningConfings= winningConfingService.findAllWinningConfing(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "prizeTime"));
		List<WinningConfingView> result = new ArrayList<>();
		for(WinningConfing winningConfing:winningConfings.getContent()) {
			if(winningConfings != null) {
				result.add(new WinningConfingView(winningConfing));
			}
		}
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,result,winningConfings.getTotalElements());
		
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/edit")
	public String edit(String id,  Model model) {
		logger.debug("发货请求参数：{}",id);
		WinningConfing winningConfing = winningConfingService.findById(id);
		winningConfing.setId(id);
		model.addAttribute("winningConfing", winningConfing);
		return ACTIVITY_EDITWINNING_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/save")
	@ResponseBody
	public RetResult<Object> saveEdit(WinningConfingView winningConfingView){
		logger.debug("发货确认请求参数对象：{}",winningConfingView.toString());
		try {
			winningConfingService.saveOrUpdate(winningConfingView);
		} catch (Exception e) {
			logger.error("设备批次更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
}
