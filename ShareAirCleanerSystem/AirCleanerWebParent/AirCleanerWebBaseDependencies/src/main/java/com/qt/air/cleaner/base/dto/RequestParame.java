package com.qt.air.cleaner.base.dto;

import java.util.Map;

public class RequestParame {
	Map<String,String> data;
	
	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	private Page page;
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public class Page{
		Integer page = 1;
		Integer limit = 20;
		Integer start = 0;
		Integer end = 0;
		public Integer getStart() {
			
			return (this.page-1) * limit;
		}
		public void setStart(Integer start) {
			this.start = start;
		}
		public Integer getEnd() {
			return ((this.page-1)*limit) + limit;
		}
		public void setEnd(Integer end) {
			this.end = end;
		}
		public Integer getPage() {
			return page;
		}
		public void setPage(Integer page) {
			this.page = page;
		}
		public Integer getLimit() {
			return limit;
		}
		public void setLimit(Integer limit) {
			this.limit = limit;
		}
		
	}
}
