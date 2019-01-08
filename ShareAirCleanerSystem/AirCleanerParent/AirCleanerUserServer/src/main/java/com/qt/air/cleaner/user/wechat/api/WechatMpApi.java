package com.qt.air.cleaner.user.wechat.api;

/**
 * 微信官方公众号相关API列表
 * 
 * @author Jansan.MA
 *
 */
public interface WechatMpApi {
	/**
	 * 获取微信code
	 */
	String oauth2_access_code = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 获取微信token[非普通token],使用code得到oauth2方式的 access_token,据此获取用户信息,openId,userInfo etc...
	 */
	String oauth2_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token";

	/**
	 * 刷新token[非普通token]
	 */
	String oauth2_refresh_token = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

	/**
	 * 验证token是否过期[非普通token]
	 */
	String sns_auth_token = "https://api.weixin.qq.com/sns/auth";

	/**
	 * 获取用户信息,URL scope需为userinfo
	 */
	String sns_userinfo = "https://api.weixin.qq.com/sns/userinfo";

	/***
	 * 获取普通token
	 */
	String cgibin_token = "https://api.weixin.qq.com/cgi-bin/token";

	/***
	 * 获取jsapi ticket
	 */
	String cgi_bin_ticket_getticket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

	/***
	 * 获取消息模板ID
	 */
	String cgibin_add_template = "https://api.weixin.qq.com/cgi-bin/template/api_add_template";

	/***
	 * 设置行业属性
	 */
	String cgibin_set_industry = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry";

	/***
	 * 发送微信推送通知
	 */
	String cgibin_send_data = "https://api.weixin.qq.com/cgi-bin/message/template/send";
	
	
}
