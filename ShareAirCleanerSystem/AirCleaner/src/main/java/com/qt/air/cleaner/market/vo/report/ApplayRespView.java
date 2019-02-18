package com.qt.air.cleaner.market.vo.report;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class ApplayRespView implements Serializable {
	private static final long serialVersionUID = -3746932403901173734L;
	JSONObject respBar = null;
	JSONObject respPie = null;
	public JSONObject getRespBar() {
		return respBar;
	}
	public void setRespBar(JSONObject respBar) {
		this.respBar = respBar;
	}
	public JSONObject getRespPie() {
		return respPie;
	}
	public void setRespPie(JSONObject respPie) {
		this.respPie = respPie;
	}
	
}
