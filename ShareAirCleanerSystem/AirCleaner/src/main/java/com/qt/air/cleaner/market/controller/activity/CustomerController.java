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
import com.qt.air.cleaner.market.domain.activity.Customer;
import com.qt.air.cleaner.market.service.activity.CustomerService;
import com.qt.air.cleaner.market.vo.activity.CustomerView;
import com.qt.air.cleaner.vo.common.PageInfo;

@Controller
@RequestMapping("market/customer")
public class CustomerController {
	private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final String  CUSTOMER_INDEX = "view/market/activity/queryCustomerIndex",
    		              CUSTOMER_EDIT = "view/market/activity/editCustomerIndex";
	@Autowired
	CustomerService customerService;
	
	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {	
		return CUSTOMER_INDEX;
	}
	
	@RequestMapping (method = RequestMethod.GET,path = "/edit")
	public String edit(String id,  Model model) {
		logger.debug("详情请求参数：{}",id);
		Customer customer = customerService.findById(id);
		customer.setId(id);
		model.addAttribute("customer", customer);
		return CUSTOMER_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/page")
	@ResponseBody
	public PageInfo<CustomerView> page(Integer page,Integer limit,CustomerView params){
		logger.debug("用户列表分页查询请求参数对象：{}",params.toString());
		Page<Customer> customers= customerService.findAllCustomer(params, 
				new PageRequest(page-1, limit, Sort.Direction.DESC, "createTime"));
		List<CustomerView> result = new ArrayList<>();
		for(Customer customer:customers.getContent()){
			if(customers != null){
				result.add(new CustomerView(customer));
			}
		}
		return new PageInfo<>(0, Constants.RESULT_PAGE_MSG, result, customers.getTotalElements());
	}

}
