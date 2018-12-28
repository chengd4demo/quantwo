package com.qt.air.cleaner.market.service.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.qt.air.cleaner.market.domain.account.WeiXinNotity;
import com.qt.air.cleaner.market.vo.account.WeiXinNotityView;
public interface WeiXinNotityService {
	
	/**
	 * 微信消费记录分页查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<WeiXinNotity> findAllWeiXinNotity(WeiXinNotityView WeiXinNotityView, Pageable pageable);

}
