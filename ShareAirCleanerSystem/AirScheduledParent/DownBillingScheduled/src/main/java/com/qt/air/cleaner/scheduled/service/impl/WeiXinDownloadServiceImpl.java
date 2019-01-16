package com.qt.air.cleaner.scheduled.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.qt.air.cleaner.scheduled.domain.BillingRefund;
import com.qt.air.cleaner.scheduled.domain.BillingSuccess;
import com.qt.air.cleaner.scheduled.domain.WeiXinDownload;
import com.qt.air.cleaner.scheduled.repository.BillingRefundRepository;
import com.qt.air.cleaner.scheduled.repository.BillingSuccessRepository;
import com.qt.air.cleaner.scheduled.repository.WeiXinDownloadRepository;
import com.qt.air.cleaner.scheduled.service.WeiXinDownloadService;

@Service
public class WeiXinDownloadServiceImpl implements WeiXinDownloadService {
	protected Logger logger = LoggerFactory.getLogger(WeiXinDownloadServiceImpl.class);
	@Resource
	private WeiXinDownloadRepository weiXinDownloadRepository;
	@Resource
	private BillingSuccessRepository billingSuccessRepository;
	@Resource
	private BillingRefundRepository billingRefundRepository;
	
	@Value("${o2.wechat.subscription.app.id}")
	public String appId;
	@Value("${o2.wechat.subscription.mah.id}")
	public String mahId;
	@Value("${o2.wechat.subscription.api.secret}")
	public String apiSecret;
	@Value("${o2.wechat.subscription.notify.url}")
	public String notifyUrl;
	WXPay wxPay = null;
	@PostConstruct
	public void init() {

		WXPayConfig config = new WXPayConfig() {

			public IWXPayDomain getWXPayDomain() {

				IWXPayDomain wxPayDomain = new IWXPayDomain() {

					@Override
					public void report(String domain, long elapsedTimeMillis, Exception ex) {

					}

					@Override
					public DomainInfo getDomain(WXPayConfig config) {

						DomainInfo info = new DomainInfo(WXPayConstants.DOMAIN_API, true);
						return info;
					}

				};
				return wxPayDomain;
			}

			public String getMchID() {

				return mahId;
			}

			@Override
			public String getKey() {

				return apiSecret;
			}

			public InputStream getCertStream() {

				return null;
			}

			@Override
			public String getAppID() {

				return appId;
			}
		};
		try {
			wxPay = new WXPay(config, notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startDownloadForSuccess(Date billDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(billDate);
		calendar.add(Calendar.DATE, -1);
		billDate = calendar.getTime();
		
		Map<String, String> reqData = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		reqData.put("bill_date", format.format(billDate));
		reqData.put("bill_type", "SUCCESS");
		
		WeiXinDownload download = new WeiXinDownload();
		download.setAppId(appId);
		download.setMchNo(mahId);
		download.setDownloadDate(billDate);
		download.setType(WeiXinDownload.WEIXIN_DOWNLOAD_TYPE_SUCCESS);
		download.setCreater("default");
		download.setCreateTime(Calendar.getInstance().getTime());
		WeiXinDownload weiXinDownload = this.existWeiXinDonwloadRecord(download);
		if (weiXinDownload == null
				|| (weiXinDownload != null && !StringUtils.equals(weiXinDownload.getReturnMsg(), "ok")
						&& !StringUtils.equals(weiXinDownload.getReturnCode(), WXPayConstants.SUCCESS))) {
			if (weiXinDownload == null) {
				weiXinDownload = download;
			}
			try {
				Map<String, String> resData = wxPay.downloadBill(reqData);
				if (StringUtils.equals(resData.get("return_msg"), "ok")
						&& StringUtils.equals(resData.get("return_code"), "SUCCESS")) {
					String data = resData.get("data");
					String[] splitData = StringUtils.split(data, "\r\n");
					List<BillingSuccess> successList = new ArrayList<BillingSuccess>();
					for (int index = 1; index < splitData.length - 2; index++) {
						BillingSuccess success = new BillingSuccess(splitData[index]);
						BillingSuccess existSuccess = billingSuccessRepository.findByTransactionId(success.getTransactionId());
						if (existSuccess == null) {
							success.setCreater("default");
							success.setCreateTime(Calendar.getInstance().getTime());
							billingSuccessRepository.save(success);
							successList.add(success);
						}
					}
					weiXinDownload.setSuccess(successList);
					weiXinDownload.setData(splitData[splitData.length - 1]);
				}
				weiXinDownload.setReturnMsg(resData.get("return_msg"));
				weiXinDownload.setReturnCode(resData.get("return_code"));
				weiXinDownloadRepository.saveAndFlush(weiXinDownload);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("自动下载前日微信对账单异常:{}",e.getMessage());
			}
		}
	}
	
	private WeiXinDownload existWeiXinDonwloadRecord(WeiXinDownload download) {
		return weiXinDownloadRepository.findByAppIdAndMchNoAndDownloadDateAndTypeAndRemoved(appId, mahId, download.getType(), download.getDownloadDate(), false);
	}

	@Override
	public void startDownloadForRefund(Date billDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(billDate);
		calendar.add(Calendar.DATE, -1);
		billDate = calendar.getTime();
		
		Map<String, String> reqData = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		reqData.put("bill_date", format.format(billDate));
		reqData.put("bill_type", "REFUND");
		
		WeiXinDownload download = new WeiXinDownload();
		download.setAppId(appId);
		download.setMchNo(mahId);
		download.setDownloadDate(Calendar.getInstance().getTime());
		download.setType(WeiXinDownload.WEIXIN_DOWNLOAD_TYPE_REFUND);

		download.setCreater("default");
		download.setCreateTime(Calendar.getInstance().getTime());
		
		WeiXinDownload weiXinDownload = this.existWeiXinDonwloadRecord(download);
		if (weiXinDownload == null
				|| (weiXinDownload != null && !StringUtils.equals(weiXinDownload.getReturnMsg(), "ok")
						&& !StringUtils.equals(weiXinDownload.getReturnCode(), WXPayConstants.SUCCESS))) {
			if (weiXinDownload == null) {
				weiXinDownload = download;
			}
			try {
				Map<String, String> resData = wxPay.downloadBill(reqData);
				if (StringUtils.equals(resData.get("return_msg"), "ok")
						&& StringUtils.equals(resData.get("return_code"), "SUCCESS")) {
					String data = resData.get("data");
					String[] splitData = StringUtils.split(data, "\r\n");
					List<BillingRefund> refundList = new ArrayList<BillingRefund>();
					for (int index = 1; index < splitData.length - 2; index++) {
						BillingRefund refund = new BillingRefund(splitData[index]);
						BillingRefund existRefund = billingRefundRepository.findByTransactionId(refund.getTransactionId());
						if (existRefund == null) {
							refund.setCreater("default");
							refund.setCreateTime(Calendar.getInstance().getTime());
							billingRefundRepository.save(refund);
						}
					}
					weiXinDownload.setRefund(refundList);
					weiXinDownload.setData(splitData[splitData.length - 1]);
				}
				weiXinDownload.setReturnMsg(resData.get("return_msg"));
				weiXinDownload.setReturnCode(resData.get("return_code"));
				weiXinDownloadRepository.saveAndFlush(weiXinDownload);
			} catch (Exception e) {
				logger.error("账单记录和通知记录进行对账处理异常:{}",e.getMessage());
				e.printStackTrace();
			}
			
		}
	}

}
