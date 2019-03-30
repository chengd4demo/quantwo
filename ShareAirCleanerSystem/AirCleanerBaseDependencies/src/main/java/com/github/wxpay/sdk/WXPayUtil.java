
package com.github.wxpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.qt.air.cleaner.base.utils.SHA1Util;

public class WXPayUtil {
	
	static Logger logger = LoggerFactory.getLogger(WXPayUtil.class);
	
	private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static final Random RANDOM = new SecureRandom();
	
	
	public static Map<String, String> xmlToMap(String strXML) throws Exception {
		
		try {
			Map<String, String> data = new HashMap<String, String>();
			DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(), element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				
			}
			return data;
		} catch (Exception ex) {
			logger.warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}",
			        ex.getMessage(), strXML);
			throw ex;
		}
		
	}
	
	
	public static String mapToXml(Map<String, String> data) throws Exception {
		
		org.w3c.dom.Document document = WXPayXmlUtil.newDocument();
		org.w3c.dom.Element root = document.createElement("xml");
		document.appendChild(root);
		for (String key : data.keySet()) {
			String value = data.get(key);
			if (value == null) {
				value = "";
			}
			value = value.trim();
			org.w3c.dom.Element filed = document.createElement(key);
			filed.appendChild(document.createTextNode(value));
			root.appendChild(filed);
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(document);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		String output = writer.getBuffer().toString(); 
		try {
			writer.close();
		} catch (Exception ex) {
		}
		return output;
	}
	
	
	public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
		
		return generateSignedXml(data, key, SignType.MD5);
	}
	
	
	public static String generateSignedXml(final Map<String, String> data, String key, SignType signType)
	        throws Exception {
		
		String sign = generateSignature(data, key, signType);
		data.put(WXPayConstants.FIELD_SIGN, sign);
		return mapToXml(data);
	}
	
	
	public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
		
		Map<String, String> data = xmlToMap(xmlStr);
		if (!data.containsKey(WXPayConstants.FIELD_SIGN)) { return false; }
		String sign = data.get(WXPayConstants.FIELD_SIGN);
		return generateSignature(data, key).equals(sign);
	}
	
	
	public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
		
		return isSignatureValid(data, key, SignType.MD5);
	}
	
	
	public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
		
		if (!data.containsKey(WXPayConstants.FIELD_SIGN)) { return false; }
		String sign = data.get(WXPayConstants.FIELD_SIGN);
		return generateSignature(data, key, signType).equals(sign);
	}
	
	
	public static String generateSignature(final Map<String, String> data, String key) throws Exception {
		
		return generateSignature(data, key, SignType.MD5);
	}
	
	
	public static String generateSignature(final Map<String, String> data, String key, SignType signType)
	        throws Exception {
		
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		String flag = "";
		for (String k : keyArray) {
			if (k.equals(WXPayConstants.FIELD_SIGN)) {
				logger.debug("忽略参数：" + k);
				continue;
			}
			if (data.get(k).trim().length() > 0) {
				sb.append(flag).append(k).append("=").append(data.get(k).trim());
				flag = "&";
			}
		}
		if (StringUtils.isNotBlank(key)) {
			sb.append("&key=").append(key);
		}
		logger.debug("签名参数：" + sb);
		logger.debug("签名格式：" + signType);
		if (SignType.MD5.equals(signType)) {
			logger.debug("生成签名sign：" + MD5(sb.toString()).toUpperCase());
			return MD5(sb.toString()).toUpperCase();
		} else if (SignType.HMACSHA256.equals(signType)) {
			return HMACSHA256(sb.toString(), key);
		} else {
			throw new Exception(String.format("Invalid sign_type: %s", signType));
		}
	}
	
	public static String newGenerateSignature(final Map<String, String> data, String key, SignType signType)
	        throws Exception {
		
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		String flag = "";
		for (String k : keyArray) {
			if (k.equals(WXPayConstants.FIELD_SIGN) || k.equals(WXPayConstants.FIELD_SIGN_TYPE)) {
				logger.debug("忽略参数：" + k);
				continue;
			}
			if (data.get(k).trim().length() > 0) {
				sb.append(flag).append(k).append("=").append(data.get(k).trim());
				flag = "&";
			}
		}
		if (StringUtils.isNotBlank(key)) {
			sb.append("&key=").append(key);
		}
		logger.debug("签名参数：" + sb);
		logger.debug("签名格式：" + signType);
		if (SignType.MD5.equals(signType)) {
			logger.debug("生成签名sign：" + MD5(sb.toString()).toUpperCase());
			return MD5(sb.toString()).toUpperCase();
		} else if (SignType.HMACSHA256.equals(signType)) {
			return HMACSHA256(sb.toString(), key);
		} else {
			throw new Exception(String.format("Invalid sign_type: %s", signType));
		}
	}
	
	public static String generateSignatureBySHA1(final Map<String, String> data)
	        throws Exception {
		
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		String flag = "";
		for (String k : keyArray) {
			if (k.equals(WXPayConstants.FIELD_SIGN)) {
				continue;
			}
			if (data.get(k).trim().length() > 0) {
				sb.append(flag).append(k).append("=").append(data.get(k).trim());
				flag = "&";
			}
		}
		logger.debug("签名字符串：" + sb);
		return SHA1Util.encode(sb.toString());
	}
	
	
	public static String generateNonceStr() {
		
		char[] nonceChars = new char[32];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}
	
	
	public static String MD5(String data) throws Exception {
		
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}
	
	
	public static String HMACSHA256(String data, String key) throws Exception {
		
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}
	
	
	public static Logger getLogger() {
		
		Logger logger = LoggerFactory.getLogger("wxpay java sdk");
		return logger;
	}
	
	
	public static long getCurrentTimestamp() {
		
		return System.currentTimeMillis() / 1000;
	}
	
	
	public static long getCurrentTimestampMs() {
		
		return System.currentTimeMillis();
	}
	
}
