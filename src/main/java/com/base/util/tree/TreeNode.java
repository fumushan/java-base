package com.base.util.tree;

import java.io.Serializable;
import java.util.List;

/**
 * 树节点
 */
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	private String id;

	/**
	 * 节点上级ID
	 */
	private String parentId;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 节点资源链接
	 */
	private String href;

	/**
	 * 节点类型
	 */
	private Integer type;

	/**
	 * 节点等级
	 */
	private Integer leavel;

	/**
	 * 节点拥有的子节点集合
	 */
	private List<TreeNode> children;

	public TreeNode() {
		super();
	}

	public TreeNode(String id, String parentId, String name, String href, Integer type, Integer leavel) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.href = href;
		this.type = type;
		this.leavel = leavel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLeavel() {
		return leavel;
	}

	public void setLeavel(Integer leavel) {
		this.leavel = leavel;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

}
