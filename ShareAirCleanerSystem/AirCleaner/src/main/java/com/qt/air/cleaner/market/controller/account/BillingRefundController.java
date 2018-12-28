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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.market.domain.account.BillingRefund;
import com.qt.air.cleaner.market.service.account.BillingRefundService;
import com.qt.air.cleaner.market.vo.account.BillingRefundView;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.common.PageInfo;
import com.qt.air.cleaner.vo.common.RetResponse;
import com.qt.air.cleaner.vo.common.RetResult;

@Controller
@RequestMapping("market/billingRefund")
public class BillingRefundController {
	private final Logger logger = LoggerFactory.getLogger(BillingRefundController.class);
	private final String ACCOUNT_BILLINGREFUND_INDEX = "view/market/account/queryBillingRefundIndex";
				  String ACCOUNT_BILLINGREFUND_EDITAUDIT_INDEX ="view/market/account/editAuditBillingRefundIndex";
	
	
	@Autowired
	BillingRefundService billingRefundService;
	
	@RequestMapping(method = RequestMethod.GET,path = "/index")
	public String index() {
		return ACCOUNT_BILLINGREFUND_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/page")
	@ResponseBody
	public PageInfo<BillingRefundView> page(Integer page, Integer limit,BillingRefundView billingRefundView){
		logger.debug("退款记录分页查询请求参数对象：{}",billingRefundView.toString());
		Page<BillingRefund> billingRefunds = billingRefundService.findAllBillingRefund(billingRefundView, new PageRequest(page-1,limit, Sort.Direction.DESC, "createTime"));
		List<BillingRefundView> result = new ArrayList<>();
		for(BillingRefund billingRefund:billingRefunds.getContent()) {
			if(billingRefunds!=null) {
				result.add(new BillingRefundView(billingRefund));
			}
		}
		return new PageInfo<>(0,Constants.RESULT_PAGE_MSG,result,billingRefunds.getTotalElements());
		
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/editAudit")
	public String edit(String id, Model model) {
		logger.debug("退款记录请求参数：{}",id);
		BillingRefund result = billingRefundService.findByIdRemoved(id,false);
		BillingRefundView billingRefund = new BillingRefundView();
		billingRefund.setId(result.getId());
		billingRefund.setRejectReasonId(result.getBillingRefundReason().getId());
		model.addAttribute("billingRefund", billingRefund);		
		return ACCOUNT_BILLINGREFUND_EDITAUDIT_INDEX;
	}
	
	@RequestMapping(method = RequestMethod.POST,path = "/refund")
	@ResponseBody
	public RetResult<Object> billingAudit (BillingRefundView billingRefundview){
		String id = billingRefundview.getId();
		logger.debug("退款确认请求参数Id：{}",id);
		if(billingRefundview == null || StringUtils.isEmpty(id))
			return RetResponse.makeErrRsp(ErrorCodeEnum.ES_4001.getMessage());
		try {
			billingRefundService.updateState(billingRefundview);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("退款确认异常：{}",e.getMessage());
			return RetResponse.makeErrRsp(e.getMessage());
		}
		return RetResponse.makeOKRsp();
		
	}
}
