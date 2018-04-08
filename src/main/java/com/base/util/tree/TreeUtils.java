package com.base.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建树型结构数据服务类
 */
public class TreeUtils {

	/**
	 * 使用递归方法建树
	 */
	public static List<TreeNode> buildTree(List<TreeNode> treeNodes) {
		List<TreeNode> trees = new ArrayList<TreeNode>();
		for (TreeNode treeNode : treeNodes) {
			if (treeNode.getParentId() == null) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 */
	public static TreeNode findChildren(TreeNode treeNode, List<TreeNode> treeNodes) {
		for (TreeNode tree : treeNodes) {
			if (treeNode.getId().equals(tree.getParentId())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<TreeNode>());
				}
				treeNode.getChildren().add(findChildren(tree, treeNodes));
			}
		}
		return treeNode;
	}
}
