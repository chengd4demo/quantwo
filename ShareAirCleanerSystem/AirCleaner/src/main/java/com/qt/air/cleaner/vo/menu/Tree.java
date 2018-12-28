package com.qt.air.cleaner.vo.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Tree {
	private StringBuffer treeJson = new StringBuffer();
	private List<Node> nodes;
	public Tree(List<Node> nodes){
		this.nodes = nodes;
	}
	
	public String buildTree(){
		treeJson.append("[");
		int i = 0;
		for (Node node : nodes) {
			String id = node.getId();
			if (node.getParentId() == null) {
				if(i!=0){
					treeJson.append(",{\"label\":" + "\"" + node.getName()+ "\",");
				}else {
					treeJson.append("{\"label\":" + "\"" + node.getName()+ "\",");
				}
				treeJson.append("\"spread\":" + false + ",");
				if(node.isChecked()) {
					treeJson.append("\"checked\":" + true + ",");
				}
				if(StringUtils.isNotEmpty(node.getUrl())) {
					treeJson.append("\"id\":" + "\"" + id+ "\"");
				} else {
					treeJson.append("\"id\":" + "\"" + id+ "\",");
				}
				build(node);
				i=i+1;
				if(i!=nodes.size()-1) {
					treeJson.append("}");
				}
			}
		}
		treeJson.append("]");
		return treeJson.toString();
	}

	private void build(Node node){
		List<Node> children = getChildren(node);
		int i = 0;
		if (!children.isEmpty()) {
			treeJson.append("\"children\":[");
			for (Node child : children) {
				String id = child.getId();
				if(i!=0){
				treeJson.append(",{\"label\":" + "\"" + child.getName()+ "\",");
				} else {
					treeJson.append("{\"label\":" + "\"" + child.getName()+ "\",");
					i=i+1;
				}
				if(node.isChecked()) {
					treeJson.append("\"checked\":" + true + ",");
				}
				treeJson.append("\"spread\":" + false + ",");
				treeJson.append("\"id\":" + "\"" + id+ "\"}");
				build(child);
			}
			treeJson.append("]");
		}
	}
	private List<Node> getChildren(Node node){
		List<Node> children = new ArrayList<Node>();
		String id = node.getId();
		for (Node child : nodes) {
			if (id.equals(child.getParentId())) {
				children.add(child);
			}
		}
		return children;
	}

}
