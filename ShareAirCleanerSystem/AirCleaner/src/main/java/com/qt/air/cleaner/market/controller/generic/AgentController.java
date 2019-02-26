package com.qt.air.cleaner.market.controller.generic;

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
import com.qt.air.cleaner.market.domain.generic.Agent;
import com.qt.air.cleaner.market.service.generic.AgentService;
import com.qt.air.cleaner.market.vo.generic.AgentView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/agent")
public class AgentController {
	private final Logger logger = LoggerFactory.getLogger(AgentController.class);
	private final String GENERIC_AGENT_INDEX = "view/market/generic/queryAgentIndex",
			GENERIC_AGENT_EDIT = "view/market/generic/editAgentIndex";
	@Autowired
	AgentService agentService;
	@RequestMapping(method = RequestMethod.GET, path = "/index")
	public String index() {
		return GENERIC_AGENT_INDEX;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<AgentView> page(Integer page,Integer limit,AgentView params){
		logger.debug("代理商信息分页查询请求参数对象：{}",params.toString());
		Page<Agent> agents= agentService.findAllAgent(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<AgentView> result = new ArrayList<>();
		for(Agent agent:agents.getContent()){
			if(agent != null){
				result.add(new AgentView(agent));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, agents.getTotalElements());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/edit")
	public String edit(String id,  Model model) {
		AgentView agentView = null;
		if (StringUtils.isNotBlank(id)) {
			Agent agent = agentService.findById(id);
			if(agent != null) {
				agentView = new AgentView(agent);
				agentView.setLegend(Constants.AGENT_EDIT_LEGEND);
			}
		} else {
			agentView = new AgentView();
			agentView.setLegend(Constants.AGENT_ADD_LEGEND);
		}
		model.addAttribute("agent", agentView);
		return GENERIC_AGENT_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/edit/save")
	@ResponseBody
	public RetResult<Object>  saveEdit(AgentView agentView){
		logger.debug("代理商信息更新请求参数对象：{}",agentView.toString());
		try {
			agentService.saveOrUpdate(agentView);
		} catch (Exception e) {
			logger.error("代理商信息更新报错异常:{}", e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/{id}")
	@ResponseBody
	public RetResult<Object>  delete(@PathVariable("id") String id){
		logger.debug("代理商信息删除请求参数{}" + id);
		boolean isOk = false;
		try {
			isOk = agentService.delete(id);
		} catch (Exception e) {
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_0000.getMessage());
		}
		if(isOk) {
			return RetResponse.makeOKRsp();
		} else {
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_9014.getMessage());
		}
	}
		
	@RequestMapping(method = RequestMethod.GET, path = "/list")
	@ResponseBody
	public List<AgentView> list(String type) {
		List<Agent> agentList = agentService.findAll(type);
		List<AgentView> result = new ArrayList<>();
		for(Agent agent : agentList){
			result.add(new AgentView(agent));
		}
		return result;
	}

}
