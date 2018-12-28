package com.qt.air.cleaner.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qt.air.cleaner.base.dto.RequestParame;
import com.qt.air.cleaner.base.dto.ResultCode;
import com.qt.air.cleaner.base.dto.ResultInfo;
import com.qt.air.cleaner.base.exception.BusinessRuntimeException;
import com.qt.air.cleaner.order.domain.AccountInBound;
import com.qt.air.cleaner.order.repository.AccountInBoundRepository;
import com.qt.air.cleaner.order.repository.TraderRepository;
import com.qt.air.cleaner.order.service.AccountInBoundService;
import com.qt.air.cleaner.order.vo.AccountInBoundView;

@RestController
@Transactional
public class AccountInBoundServiceImpl implements AccountInBoundService {
	private static final Logger logger = LoggerFactory.getLogger(AccountInBoundServiceImpl.class);
	@Resource
	private TraderRepository traderRepository;
	@Resource
	private AccountInBoundRepository accountInBoundRepository;
	/**
	 * 收入明细
	 * 
	 * @param requestParame
	 * @return
	 * @throws BusinessRuntimeException
	 * 
	 */
	@Override
	public ResultInfo queryAccountInbound(@RequestBody RequestParame requestParame) throws BusinessRuntimeException {
		String weixin = requestParame.getData().get("weixin");
		Integer start = requestParame.getPage().getStart();
		Integer end = requestParame.getPage().getEnd();
		Pageable pageable = new PageRequest(start, end, Sort.Direction.DESC, "createTime");
		logger.info("execute method queryAccountInbound() param --> weixin:{}", requestParame);
		Specification<AccountInBound> specification = new Specification<AccountInBound>() {			
			@Override
			public Predicate toPredicate(Root<AccountInBound> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				if (StringUtils.isNotBlank(weixin)) { 
					Predicate p1 = cb.equal(root.get("weixin"), weixin);
					conditions.add(p1);
				}
				Predicate p4 = cb.equal(root.get("removed"), false);
				conditions.add(p4);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}

		};
		Page<AccountInBound> page = accountInBoundRepository.findAll(specification, pageable);
		List<AccountInBoundView> result = new ArrayList<AccountInBoundView>();
		if(page != null) {
			AccountInBoundView view = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (AccountInBound inBound : page.getContent()){
				view = new AccountInBoundView();
				view.setCode(inBound.getCode());
				view.setName(inBound.getName());
				view.setType(inBound.getType());
				view.setAmount(inBound.getAmount());
				view.setCreateTime(inBound.getBilling().getCreateTime());
				Integer costTime = inBound.getBilling().getCostTime() / 60;
				view.setCostTime(costTime.toString());
				view.setUnitPrice(inBound.getBilling().getAmount());
				view.setDiscountStr(inBound.getBilling().getDiscountStr());
				view.setCreateTimeStr(dateFormat.format(inBound.getBilling().getCreateTime()));
				if(StringUtils.equals("投资商", inBound.getType())){
					String address = traderRepository.findByWeixinAndDeviceId(weixin, inBound.getBilling().getDeviceId());
					if(StringUtils.isEmpty(address)) {
						view.setAddress("-");
					}else {
						view.setAddress(address);
					}
				}
				result.add(view);
			}
		}
		return new ResultInfo(String.valueOf(ResultCode.SC_OK), "success", result);
	}

}
