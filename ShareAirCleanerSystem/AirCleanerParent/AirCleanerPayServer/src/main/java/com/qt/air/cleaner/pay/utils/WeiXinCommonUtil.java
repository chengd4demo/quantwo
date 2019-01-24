package com.qt.air.cleaner.pay.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qt.air.cleaner.pay.domain.Token;
import com.qt.air.cleaner.pay.domain.WeixinOauth2Token;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class WeiXinCommonUtil {
	static Logger logger = LoggerFactory.getLogger(WeiXinCommonUtil.class);
	public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
			 create_qrcode_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s",
		        show_qrcode_url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s",
		        pay_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
		        jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		
		JSONObject jsonObject = null;
		try {
			TrustManager[] tm = {
			        new MyX509TrustManager()
			};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(requestMethod);
			
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("请求地址错误：" + requestUrl, ce);
		} catch (Exception ex) {
			logger.error("执行求情【" + requestUrl + "】失败！", ex);
		}
		return jsonObject;
	}
	
	public static Token getToken(String appId, String appSecret) {
		
		Token token = null;
		String requestUrl = String.format(token_url, appId, appSecret);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		
		if (null != jsonObject) {
			try {
				token = new Token();
				token.setAccessToken(jsonObject.getString("access_token"));
				token.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				token = null;
				logger.error("获取TOKEN时发生错误,错误代码:{}， 错误信息:{}", jsonObject.getInt("errcode"),
				        jsonObject.getString("errmsg"));
			}
		}
		return token;
	}
	
	public static String createPermanentQRCode(String accessToken, String sceneId) {
		
		String ticket = null;
		String requestUrl = String.format(create_qrcode_url, accessToken);
		String jsonMsg = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": %s}}}";
		JSONObject jsonObject = httpsRequest(requestUrl, "POST", String.format(jsonMsg, sceneId));
		
		if (null != jsonObject) {
			try {
				ticket = jsonObject.getString("ticket");
				logger.info("生成二维码信息成功：" + ticket);
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				logger.error("生成二维码信息错误，错误代码{}，错误信息{}", errorCode, errorMsg);
			}
		}
		return ticket;
	}
	
	public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
		
		WeixinOauth2Token wat = null;
		logger.info("appId:{}",appId);
		logger.info("appSecret:{}",appSecret);
		logger.info("code:{}",code);
		
		String requestUrl = String.format(pay_token_url, appId, appSecret, code);
		logger.info("获取微信网页授权地址：" + requestUrl);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		logger.info("获取微信网页授权结果：" + jsonObject);
		if (null != jsonObject) {	
			try {
				wat = new WeixinOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			} catch (Exception e) {
				wat = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				logger.error("微信支付时发生错误：错误代码{}，错误信息{}", errorCode, errorMsg);
			}
		}
		return wat;
	}
	
	public static String getJsApiTicket(String accessToken) {
		
		String requestUrl = String.format(jsapi_ticket_url, accessToken);
		logger.debug("获取JSAPI票据凭证地址：" + requestUrl);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		String jsapiTicket = "";
		if (jsonObject != null) {
			jsapiTicket = jsonObject.getString("ticket");
			logger.debug("获取JSAPI票据凭证：" + jsapiTicket);
		}
		return jsapiTicket;
	}
	
	public static String getQRCode(String ticket, String savePath, String sceneId) {
		
		String filePath = null, fileName = "";
		String requestUrl = String.format(show_qrcode_url, urlEncodeUTF8(ticket));
		try {
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			
			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			filePath = savePath + sceneId + ".jpg";
			fileName = sceneId + ".jpg";
			
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			filePath = null;
		}
		return fileName;
	}
	
	public static String urlEncodeUTF8(String source) {
		
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
