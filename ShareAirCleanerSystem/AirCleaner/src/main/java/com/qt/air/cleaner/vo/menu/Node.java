package com.qt.air.cleaner.vo.menu;

public class Node {
	private String id;
	private String parentId;
	private String name;
	private String url;
	private boolean checked = false;
	public String getId() {
		return id;
	}
	public String getParentId() {
		return parentId;
	}
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
