package com.qt.air.cleaner.vo.security;

public class TreeView {
	private String label;
	private boolean spread = false;
	private boolean disabled = false;
	private boolean checked = false;
	public String getLabel() {
		return label;
	}
	public boolean isSpread() {
		return spread;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public boolean isChecked() {
		return checked;
	}
	public TreeView[] getChildren() {
		return children;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setSpread(boolean spread) {
		this.spread = spread;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public void setChildren(TreeView[] children) {
		this.children = children;
	}
	private TreeView[] children;
}
