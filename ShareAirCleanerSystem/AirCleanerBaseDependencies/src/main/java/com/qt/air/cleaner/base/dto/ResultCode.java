package com.qt.air.cleaner.base.dto;

import javax.servlet.http.HttpServletResponse;

public interface ResultCode extends HttpServletResponse {
	public static final Entry R1001 = new Entry("EP1001", "is empty");
	public static final Entry R2001 = new Entry("EP2001", "param error");
	public static final Entry R3001 = new Entry("EP300", "upload images is empty");
	public static final Entry R4001 = new Entry("EP400","this customer already has views that do not need to be updated");
	public static final Entry R5001 = new Entry("EP500", "request timeout");
	public static final Entry R5002 = new Entry("EP502", "system error");	
	 public static class Entry {

	        public String code;
	        public String info;

	        public Entry(String code, String info) {
	            this.code = code;
	            this.info = info;
	        }

	        @Override
	        public String toString() {
	            return "Entry{" +
	                    "code=" + code +
	                    ", info=" + info +
	                    '}';
	        }
	    }
}
