package com.qt.air.cleaner.user.vo.mp;

import java.io.Serializable;

public class JsApiTicketResponse implements Serializable {

	private static final long serialVersionUID = -8270140618980354224L;
	private long errcode;
    private String errmsg;
    private String ticket;
    private String expires_in;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public JsApiTicketResponse() {
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

	@Override
	public String toString() {
		return "JsApiTicketResponse [errcode=" + errcode + ", errmsg=" + errmsg + ", ticket=" + ticket + ", expires_in="
				+ expires_in + ", desc=" + desc + "]";
	}

}
