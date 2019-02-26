package com.qt.air.cleaner.market.service.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qt.air.cleaner.market.domain.generic.Agent;
import com.qt.air.cleaner.market.vo.generic.AgentView;

public interface AgentService {

	/**
	 * 查询代理商信息下拉列表
	 * 
	 * @param removed
	 * @return
	 */
	List<Agent> findAll(boolean removed);
	
	/**
	 * 代理商分页模糊查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<Agent> findAllAgent(AgentView params, Pageable pageable);
	
	/**
	 * 代理商查询
	 * @param id
	 * @return
	 */
	Agent findById(String id);
	
	/**
	 * 代理商新增或修改
	 * 
	 * @param agentView
	 */
	void saveOrUpdate(AgentView agentView);
	
	/**
	 * 代理商信息删除
	 * 
	 * @param id
	 */
	void delete(String id);
}
