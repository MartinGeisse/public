/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;

/**
 * This class wraps a tree of navigation nodes. It always contains
 * at least the root node of the tree, and the root node is always
 * a folder node.
 */
public final class NavigationTree {

	/**
	 * the root
	 */
	private NavigationFolder root;
	
	/**
	 * the nodesByPath
	 */
	private Map<String, INavigationNode> nodesByPath;

	/**
	 * Constructor.
	 */
	public NavigationTree() {
		root = new NavigationFolder();
		root.setTitle("Navigation");
	}
	
	/**
	 * Getter method for the root.
	 * @return the root
	 */
	public NavigationFolder getRoot() {
		return root;
	}

	/**
	 * Setter method for the root.
	 * @param root the root to set
	 */
	public void setRoot(final NavigationFolder root) {
		this.root = root;
	}
	
	/**
	 * Initializes the path-to-node mapping.
	 */
	public void initializeNodesByPath() {
		this.nodesByPath = new HashMap<String, INavigationNode>();
		nodesByPath.put("/", root);
		initializeNodesByPath(root.getChildren(), "/");
	}
	
	/**
	 * 
	 */
	private void initializeNodesByPath(Iterable<INavigationNode> nodes, String prefix) {
		for (INavigationNode node : nodes) {
			initializeNodeByPath(node, prefix);
		}
	}

	/**
	 * 
	 */
	private void initializeNodeByPath(INavigationNode node, String prefix) {
		String path = prefix + node.getId();
		nodesByPath.put(path, node);
		if (node instanceof INavigationParentNode) {
			INavigationParentNode nodeAsParent = (INavigationParentNode)node;
			initializeNodesByPath(nodeAsParent, path + "/");
		}
	}
	
	/**
	 * Getter method for the nodesByPath.
	 * @return the nodesByPath
	 */
	public Map<String, INavigationNode> getNodesByPath() {
		return nodesByPath;
	}
	
	/**
	 * Mounts the request mappers for all navigation nodes. This method
	 * is called by the framework.
	 * @param application the wicket application
	 */
	public void mountRequestMappers(AdminWicketApplication application) {
		root.mountRequestMappers(application);
	}

}
