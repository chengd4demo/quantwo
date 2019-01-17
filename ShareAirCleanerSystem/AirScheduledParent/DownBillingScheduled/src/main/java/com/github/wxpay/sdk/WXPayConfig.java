
package com.github.wxpay.sdk;

import java.io.InputStream;

public abstract class WXPayConfig {
	
	
	public abstract String getAppID();
	
	
	public abstract String getMchID();
	
	
	public abstract String getKey();
	
	
	public abstract InputStream getCertStream();
	
	
	public int getHttpConnectTimeoutMs() {
		
		return 6 * 1000;
	}
	
	
	public int getHttpReadTimeoutMs() {
		
		return 8 * 1000;
	}
	
	
	protected abstract IWXPayDomain getWXPayDomain();
	
	
	public boolean shouldAutoReport() {
		
		return true;
	}
	
	
	public int getReportWorkerNum() {
		
		return 6;
	}
	
	
	public int getReportQueueMaxSize() {
		
		return 10000;
	}
	
	
	public int getReportBatchSize() {
		
		return 10;
	}
	
}
