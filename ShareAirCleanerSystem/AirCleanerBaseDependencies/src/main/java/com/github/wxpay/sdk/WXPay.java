
package com.github.wxpay.sdk;

import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.WXPayConstants.SignType;

public class WXPay {
	
	private WXPayConfig config;
	
	private SignType signType;
	
	private boolean autoReport;
	
	private boolean useSandbox;
	
	private String notifyUrl;
	
	private WXPayRequest wxPayRequest;
	
	public WXPay(final WXPayConfig config) throws Exception {
		
		this(config, null, true, false);
	}
	
	public WXPay(final WXPayConfig config, final boolean autoReport) throws Exception {
		
		this(config, null, autoReport, false);
	}
	
	public WXPay(final WXPayConfig config, final boolean autoReport, final boolean useSandbox) throws Exception {
		
		this(config, null, autoReport, useSandbox);
	}
	
	public WXPay(final WXPayConfig config, final String notifyUrl) throws Exception {
		
		this(config, notifyUrl, true, false);
	}
	
	public WXPay(final WXPayConfig config, final String notifyUrl, final boolean autoReport) throws Exception {
		
		this(config, notifyUrl, autoReport, false);
	}
	
	public WXPay(final WXPayConfig config, final String notifyUrl, final boolean autoReport, final boolean useSandbox)
	        throws Exception {
		
		this.config = config;
		this.notifyUrl = notifyUrl;
		this.autoReport = autoReport;
		this.useSandbox = useSandbox;
		if (useSandbox) {
			this.signType = SignType.MD5;
		} else {
			this.signType = SignType.MD5;
		}
		this.wxPayRequest = new WXPayRequest(config);
	}
	
	private void checkWXPayConfig() throws Exception {
		
		if (this.config == null) { throw new Exception("config is null"); }
		if (this.config.getAppID() == null
		        || this.config.getAppID().trim().length() == 0) { throw new Exception("appid in config is empty"); }
		if (this.config.getMchID() == null
		        || this.config.getMchID().trim().length() == 0) { throw new Exception("appid in config is empty"); }
		if (this.config.getCertStream() == null) { throw new Exception("cert stream in config is empty"); }
		if (this.config.getWXPayDomain() == null) { throw new Exception("config.getWXPayDomain() is null"); }
		
		if (this.config.getHttpConnectTimeoutMs() < 10) { throw new Exception("http connect timeout is too small"); }
		if (this.config.getHttpReadTimeoutMs() < 10) { throw new Exception("http read timeout is too small"); }
		
	}
	
	public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
		
		reqData.put("appid", config.getAppID());
		reqData.put("mch_id", config.getMchID());
		reqData.put("nonce_str", WXPayUtil.generateNonceStr());
		if (SignType.MD5.equals(this.signType)) {
			reqData.put("sign_type", WXPayConstants.MD5);
		} else if (SignType.HMACSHA256.equals(this.signType)) {
			reqData.put("sign_type", WXPayConstants.HMACSHA256);
		}
		reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), this.signType));
		return reqData;
	}
	
	public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
		
		return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
	}
	
	public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
		
		String signTypeInData = reqData.get(WXPayConstants.FIELD_SIGN_TYPE);
		SignType signType;
		if (signTypeInData == null) {
			signType = SignType.MD5;
		} else {
			signTypeInData = signTypeInData.trim();
			if (signTypeInData.length() == 0) {
				signType = SignType.MD5;
			} else if (WXPayConstants.MD5.equals(signTypeInData)) {
				signType = SignType.MD5;
			} else if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
				signType = SignType.HMACSHA256;
			} else {
				throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
			}
		}
		return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
	}
	
	public String requestWithoutCert(String urlSuffix, Map<String, String> reqData, int connectTimeoutMs,
	        int readTimeoutMs) throws Exception {
		
		String msgUUID = reqData.get("nonce_str");
		String reqBody = WXPayUtil.mapToXml(reqData);
		
		String resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs,
		        autoReport);
		return resp;
	}
	
	public String requestWithCert(String urlSuffix, Map<String, String> reqData, int connectTimeoutMs,
	        int readTimeoutMs) throws Exception {
		
		String msgUUID = reqData.get("nonce_str");
		String reqBody = WXPayUtil.mapToXml(reqData);
		
		String resp = this.wxPayRequest.requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs,
		        this.autoReport);
		return resp;
	}
	
	public Map<String, String> processResponseXml(String xmlStr,Boolean validReponseSign) throws Exception {
		
		String RETURN_CODE = "return_code";
		String return_code;
		Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
		if (respData.containsKey(RETURN_CODE)) {
			return_code = respData.get(RETURN_CODE);
		} else {
			throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
		}
		
		if (return_code.equals(WXPayConstants.FAIL)) {
			return respData;
		} else if (return_code.equals(WXPayConstants.SUCCESS)) {
			if (validReponseSign) {
				if (this.isResponseSignatureValid(respData)) {
					return respData;
				} else {
					throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
				}
			} else {
				return respData;
			}
		} else {
			throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
		}
	}
	public Map<String, String> processResponseXml(String xmlStr) throws Exception {
		return processResponseXml(xmlStr,true);
	}
	
	public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
		
		return this.microPay(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_MICROPAY_URL_SUFFIX;
		} else {
			url = WXPayConstants.MICROPAY_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> microPayWithPos(Map<String, String> reqData) throws Exception {
		
		return this.microPayWithPos(reqData, this.config.getHttpConnectTimeoutMs());
	}
	
	public Map<String, String> microPayWithPos(Map<String, String> reqData, int connectTimeoutMs) throws Exception {
		
		int remainingTimeMs = 60 * 1000;
		long startTimestampMs = 0;
		Map<String, String> lastResult = null;
		Exception lastException = null;
		
		while (true) {
			startTimestampMs = WXPayUtil.getCurrentTimestampMs();
			int readTimeoutMs = remainingTimeMs - connectTimeoutMs;
			if (readTimeoutMs > 1000) {
				try {
					lastResult = this.microPay(reqData, connectTimeoutMs, readTimeoutMs);
					String returnCode = lastResult.get("return_code");
					if (returnCode.equals("SUCCESS")) {
						String resultCode = lastResult.get("result_code");
						String errCode = lastResult.get("err_code");
						if (resultCode.equals("SUCCESS")) {
							break;
						} else {
							
							if (errCode.equals("SYSTEMERROR") || errCode.equals("BANKERROR")
							        || errCode.equals("USERPAYING")) {
								remainingTimeMs = remainingTimeMs
								        - (int) (WXPayUtil.getCurrentTimestampMs() - startTimestampMs);
								if (remainingTimeMs <= 100) {
									break;
								} else {
									WXPayUtil.getLogger().info("microPayWithPos: try micropay again");
									if (remainingTimeMs > 5 * 1000) {
										Thread.sleep(5 * 1000);
									} else {
										Thread.sleep(1 * 1000);
									}
									continue;
								}
							} else {
								break;
							}
						}
					} else {
						break;
					}
				} catch (Exception ex) {
					lastResult = null;
					lastException = ex;
				}
			} else {
				break;
			}
		}
		
		if (lastResult == null) {
			throw lastException;
		} else {
			return lastResult;
		}
	}
	
	public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
		
		return this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> unifiedOrder(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
		} else {
			url = WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
		}
		if (this.notifyUrl != null) {
			reqData.put("notify_url", this.notifyUrl);
		}
		Map<String, String> requestData = this.fillRequestData(reqData);
		System.out.println("微信参数" + requestData.toString());
		String respXml = this.requestWithoutCert(url, requestData, connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> orderQuery(Map<String, String> reqData) throws Exception {
		
		return this.orderQuery(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
		} else {
			url = WXPayConstants.ORDERQUERY_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
		
		return this.reverse(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_REVERSE_URL_SUFFIX;
		} else {
			url = WXPayConstants.REVERSE_URL_SUFFIX;
		}
		String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> closeOrder(Map<String, String> reqData) throws Exception {
		
		return this.closeOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> closeOrder(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_CLOSEORDER_URL_SUFFIX;
		} else {
			url = WXPayConstants.CLOSEORDER_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> refund(Map<String, String> reqData) throws Exception {
		
		return this.refund(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_REFUND_URL_SUFFIX;
		} else {
			url = WXPayConstants.REFUND_URL_SUFFIX;
		}
		String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> refundQuery(Map<String, String> reqData) throws Exception {
		
		return this.refundQuery(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_REFUNDQUERY_URL_SUFFIX;
		} else {
			url = WXPayConstants.REFUNDQUERY_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> downloadBill(Map<String, String> reqData) throws Exception {
		
		return this.downloadBill(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_DOWNLOADBILL_URL_SUFFIX;
		} else {
			url = WXPayConstants.DOWNLOADBILL_URL_SUFFIX;
		}
		String respStr = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs)
		        .trim();
		Map<String, String> ret;
		
		if (respStr.indexOf("<") == 0) {
			ret = WXPayUtil.xmlToMap(respStr);
		} else {
			
			ret = new HashMap<String, String>();
			ret.put("return_code", WXPayConstants.SUCCESS);
			ret.put("return_msg", "ok");
			ret.put("data", respStr);
		}
		return ret;
	}
	
	public Map<String, String> report(Map<String, String> reqData) throws Exception {
		
		return this.report(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_REPORT_URL_SUFFIX;
		} else {
			url = WXPayConstants.REPORT_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return WXPayUtil.xmlToMap(respXml);
	}
	
	public Map<String, String> shortUrl(Map<String, String> reqData) throws Exception {
		
		return this.shortUrl(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_SHORTURL_URL_SUFFIX;
		} else {
			url = WXPayConstants.SHORTURL_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> authCodeToOpenid(Map<String, String> reqData) throws Exception {
		
		return this.authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(),
		        this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_AUTHCODETOOPENID_URL_SUFFIX;
		} else {
			url = WXPayConstants.AUTHCODETOOPENID_URL_SUFFIX;
		}
		String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		return this.processResponseXml(respXml);
	}
	
	public Map<String, String> sendRedPack(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url = WXPayConstants.SENDREDPACK_URL_SUFFIX;
		
		String respXml = this.requestWithCert(url, this.newFillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
		System.out.println("微信红包响应结果：" + respXml);
		return this.processResponseXml(respXml, false);
	}
	
	public Map<String, String> gethbInfo(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs)
	        throws Exception {
		
		String url = WXPayConstants.GETHBINFO_URL_SUFFIX;
		String respXml = this.requestWithCert(url, this.newFillRequestData(reqData),  config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
		System.out.println("微信红包响应结果：" + respXml);
		return this.processResponseXml(respXml, false);
	}
	
	public Map<String, String> gethbInfo(Map<String, String> reqData)
	        throws Exception {
		
		return this.gethbInfo(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	
	public Map<String, String> sendRedPack(Map<String, String> reqData) throws Exception {
		
		return this.sendRedPack(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
	}
	
	public Map<String, String> newFillRequestData(Map<String, String> reqData) throws Exception {
		if (SignType.MD5.equals(this.signType)) {
			reqData.put("sign_type", WXPayConstants.MD5);
		} else if (SignType.HMACSHA256.equals(this.signType)) {
			reqData.put("sign_type", WXPayConstants.HMACSHA256);
		}
		reqData.put("sign", WXPayUtil.newGenerateSignature(reqData, config.getKey(), this.signType));
		return reqData;
	}
}
