
package com.github.wxpay.sdk;


public abstract interface IWXPayDomain {
	
	
	abstract void report(final String domain, long elapsedTimeMillis, final Exception ex);
	
	
	abstract DomainInfo getDomain(final WXPayConfig config);
	
	static class DomainInfo {
		
		public String domain; 
		
		public boolean primaryDomain; 
		
		public DomainInfo(String domain, boolean primaryDomain) {
			
			this.domain = domain;
			this.primaryDomain = primaryDomain;
		}
		
		@Override
		public String toString() {
			
			return "DomainInfo{" + "domain='" + domain + '\'' + ", primaryDomain=" + primaryDomain + '}';
		}
	}
	
}
