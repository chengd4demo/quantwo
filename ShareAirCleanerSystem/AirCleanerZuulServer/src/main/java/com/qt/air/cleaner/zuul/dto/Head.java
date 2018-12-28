package com.qt.air.cleaner.zuul.dto;

import java.io.Serializable;

public class Head implements Serializable {
	private static final long serialVersionUID = -7919313219038781123L;
	private String partner; //允许外部接入调用唯识别号
	private String key; //签名key
	private String mdkey; //签名后字符串
	private String token; // token
	public String getPartner() {
		return partner;
	}
	public String getKey() {
		return key;
	}
	public String getMdkey() {
		return mdkey;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setMdkey(String mdkey) {
		this.mdkey = mdkey;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "Head [partner=" + partner + ", key=" + key + ", mdkey=" + mdkey + ", token=" + token + "]";
	}
	
	
}
