
package com.qt.air.cleaner.market.domain;

import java.io.Serializable;

public class Token implements Serializable{

	private static final long serialVersionUID = -175208012082000272L;

	private String accessToken;
	
	private int expiresIn;
	
	public String getAccessToken() {
		
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		
		this.accessToken = accessToken;
	}
	
	public int getExpiresIn() {
		
		return expiresIn;
	}
	
	public void setExpiresIn(int expiresIn) {
		
		this.expiresIn = expiresIn;
	}
}
