package com.qt.air.cleaner.user.vo.mp;

import java.io.Serializable;

public class TokenResponse implements Serializable {
	private static final long serialVersionUID = 2579072117915934387L;
	private String access_token;
	private String expires_in;
	private long errcode;
	private String errmsg;
	private String desc;
	
	public TokenResponse() {
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getErrcode() {
		return errcode;
	}

	public void setErrcode(long errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public String toString() {
		return "TokenResponse [access_token=" + access_token + ", expires_in=" + expires_in + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + ", desc=" + desc + "]";
	}
	
}
