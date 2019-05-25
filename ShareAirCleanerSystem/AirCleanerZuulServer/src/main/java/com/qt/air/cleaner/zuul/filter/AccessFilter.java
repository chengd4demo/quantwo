package com.qt.air.cleaner.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter {
	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
	private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
	@Override
	public boolean shouldFilter() {
		 return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String remoteAddr = this.getIpAddr(request);
		log.info("Client's -> " + remoteAddr);
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		Object accessToken = request.getParameter("accessToken");
		/*if (accessToken == null) {
			log.warn("access token is empty");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			return null;
		}*/
		ctx.getZuulRequestHeaders().put(HTTP_X_FORWARDED_FOR, remoteAddr);
		log.info("access token ok");
		return null;
	}

	/**
	 * 获取客户端真是IP
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {  
	       String ip = request.getHeader("x-forwarded-for");  
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	           ip = request.getHeader("Proxy-Client-IP");  
	       }  
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	           ip = request.getHeader("WL-Proxy-Client-IP");  
	       }  
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	           ip = request.getRemoteAddr();  
	       }  
	       //多级反向代理的话，X-Forwarded-For的值并不止一个，取X-Forwarded-For中第一个非unknown的有效IP字符串
	       if(ip != null) {
	    	   ip = ip.split(",")[0];
	       }
	       return ip;  
	   }  
}
